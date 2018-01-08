package com.mautini.assistant.demo.api;

import com.google.assistant.embedded.v1alpha2.AssistConfig;
import com.google.assistant.embedded.v1alpha2.AssistRequest;
import com.google.assistant.embedded.v1alpha2.AssistResponse;
import com.google.assistant.embedded.v1alpha2.AudioInConfig;
import com.google.assistant.embedded.v1alpha2.AudioOutConfig;
import com.google.assistant.embedded.v1alpha2.DeviceConfig;
import com.google.assistant.embedded.v1alpha2.DialogStateIn;
import com.google.assistant.embedded.v1alpha2.EmbeddedAssistantGrpc;
import com.google.assistant.embedded.v1alpha2.SpeechRecognitionResult;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.OAuth2Credentials;
import com.google.protobuf.ByteString;
import com.mautini.assistant.demo.authentication.OAuthCredentials;
import com.mautini.assistant.demo.config.AssistantConf;
import com.mautini.assistant.demo.device.Device;
import com.mautini.assistant.demo.device.DeviceModel;
import com.mautini.assistant.demo.exception.ConverseException;
import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.auth.MoreCallCredentials;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AssistantClient implements StreamObserver<AssistResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssistantClient.class);

    private CountDownLatch finishLatch = new CountDownLatch(1);

    private EmbeddedAssistantGrpc.EmbeddedAssistantStub embeddedAssistantStub;

    private ByteArrayOutputStream currentResponse = new ByteArrayOutputStream();

    // See reference.conf
    private AssistantConf assistantConf;

    /**
     * Conversation state to continue a conversation if needed
     *
     * @see <a href="https://developers.google.com/assistant/sdk/reference/rpc/google.assistant.embedded.v1alpha2#google.assistant.embedded.v1alpha2.DialogStateOut.FIELDS.bytes.google.assistant.embedded.v1alpha2.DialogStateOut.conversation_state">Google documentation</a>
     */
    private ByteString currentConversationState;

    private DeviceModel deviceModel;

    private Device device;

    public AssistantClient(OAuthCredentials oAuthCredentials, AssistantConf assistantConf, DeviceModel deviceModel,
                           Device device) {

        this.assistantConf = assistantConf;
        this.deviceModel = deviceModel;
        this.device = device;
        this.currentConversationState = ByteString.EMPTY;

        // Create a channel to the test service.
        ManagedChannel channel = ManagedChannelBuilder.forAddress(assistantConf.getAssistantApiEndpoint(), 443)
                .build();

        // Create a stub with credential
        embeddedAssistantStub = EmbeddedAssistantGrpc.newStub(channel);

        updateCredentials(oAuthCredentials);
    }

    /**
     * Get CallCredentials from OAuthCredentials
     *
     * @param oAuthCredentials the credentials from the AuthenticationHelper
     * @return the CallCredentials for the GRPC requests
     */
    private CallCredentials getCallCredentials(OAuthCredentials oAuthCredentials) {

        AccessToken accessToken = new AccessToken(
                oAuthCredentials.getAccessToken(),
                new Date(oAuthCredentials.getExpirationTime())
        );

        OAuth2Credentials oAuth2Credentials = new OAuth2Credentials(accessToken);

        // Create an instance of {@link io.grpc.CallCredentials}
        return MoreCallCredentials.from(oAuth2Credentials);
    }

    /**
     * Update the credentials used to request the api
     *
     * @param oAuthCredentials the new credentials
     */
    public void updateCredentials(OAuthCredentials oAuthCredentials) {
        embeddedAssistantStub = embeddedAssistantStub.withCallCredentials(getCallCredentials(oAuthCredentials));
    }

    public byte[] requestAssistant(byte[] request) throws ConverseException {
        try {
            // Reset the byte array
            currentResponse = new ByteArrayOutputStream();
            finishLatch = new CountDownLatch(1);

            LOGGER.info("Requesting the assistant");
            // Send the config request
            StreamObserver<AssistRequest> requester = embeddedAssistantStub.assist(this);
            requester.onNext(getConfigRequest());

            // Divide the audio request into chunks
            byte[][] chunks = divideArray(request, assistantConf.getChunkSize());

            // Send a request for each chunk
            for (byte[] chunk : chunks) {
                ByteString audioIn = ByteString.copyFrom(chunk);

                // Chunk of the request
                AssistRequest assistRequest = AssistRequest
                        .newBuilder()
                        .setAudioIn(audioIn)
                        .build();

                requester.onNext(assistRequest);
            }

            // Mark the end of requests
            requester.onCompleted();

            // Receiving happens asynchronously
            finishLatch.await(1, TimeUnit.MINUTES);

            return currentResponse.toByteArray();
        } catch (Exception e) {
            throw new ConverseException("Error requesting the assistant", e);
        }
    }

    @Override
    public void onNext(AssistResponse value) {
        try {
            if (value.getEventType() != null
                    && value.getEventType() != AssistResponse.EventType.EVENT_TYPE_UNSPECIFIED) {

                LOGGER.info("Event type : {}", value.getEventType().name());
            }

            if (value.getAudioOut() != null) {
                currentResponse.write(value.getAudioOut().getAudioData().toByteArray());
            }

            if (value.getDialogStateOut() != null) {
                currentConversationState = value.getDialogStateOut().getConversationState();

                if (value.getSpeechResultsList() != null) {
                    String userRequest = value.getSpeechResultsList().stream()
                            .map(SpeechRecognitionResult::getTranscript)
                            .collect(Collectors.joining(" "));

                    if (!userRequest.isEmpty()) {
                        LOGGER.info("Request Text : {}", userRequest);
                    }
                }
            }

            if (value.getDialogStateOut() != null
                    && value.getDialogStateOut().getSupplementalDisplayText() != null
                    && !value.getDialogStateOut().getSupplementalDisplayText().isEmpty()) {

                LOGGER.info("Response Text : {}", value.getDialogStateOut().getSupplementalDisplayText());
            }

        } catch (Exception e) {
            LOGGER.warn("Error requesting the assistant", e);
        }
    }

    @Override
    public void onError(Throwable t) {
        LOGGER.warn("Error requesting the assistant", t);
        finishLatch.countDown();
    }

    @Override
    public void onCompleted() {
        LOGGER.info("End of the response");
        finishLatch.countDown();
    }

    /**
     * Create the config message, this message must be send before the audio for each request
     *
     * @return the request to send
     */
    private AssistRequest getConfigRequest() {
        AudioInConfig audioInConfig = AudioInConfig
                .newBuilder()
                .setEncoding(AudioInConfig.Encoding.LINEAR16)
                .setSampleRateHertz(assistantConf.getAudioSampleRate())
                .build();

        AudioOutConfig audioOutConfig = AudioOutConfig
                .newBuilder()
                .setEncoding(AudioOutConfig.Encoding.LINEAR16)
                .setSampleRateHertz(assistantConf.getAudioSampleRate())
                .setVolumePercentage(assistantConf.getVolumePercent())
                .build();

        DialogStateIn.Builder dialogStateInBuilder = DialogStateIn
                .newBuilder()
                // We set the us local as default
                .setLanguageCode("en-US")
                .setConversationState(currentConversationState);

        DeviceConfig deviceConfig = DeviceConfig
                .newBuilder()
                .setDeviceModelId(deviceModel.getDeviceModelId())
                .setDeviceId(device.getId())
                .build();

        AssistConfig.Builder assistConfigBuilder = AssistConfig
                .newBuilder()
                .setDialogStateIn(dialogStateInBuilder.build())
                .setDeviceConfig(deviceConfig)
                .setAudioInConfig(audioInConfig)
                .setAudioOutConfig(audioOutConfig);

        return AssistRequest
                .newBuilder()
                .setConfig(assistConfigBuilder.build())
                .build();
    }

    /**
     * Divide an array of byte in chunks of chunkSize bytes
     *
     * @param source    the source byte array
     * @param chunkSize the size of a chunk
     * @return an array of chunks
     * @see <a href="http://stackoverflow.com/questions/3405195/divide-array-into-smaller-parts">Divide array into smaller parts</a>
     */
    private byte[][] divideArray(byte[] source, int chunkSize) {
        byte[][] ret = new byte[(int) Math.ceil(source.length / (double) chunkSize)][chunkSize];

        int start = 0;

        for (int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(source, start, start + chunkSize);
            start += chunkSize;
        }

        return ret;
    }
}
