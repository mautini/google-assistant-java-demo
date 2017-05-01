package com.mautini.assistant.demo.api;

import com.google.assistant.embedded.v1alpha1.AudioInConfig;
import com.google.assistant.embedded.v1alpha1.AudioOutConfig;
import com.google.assistant.embedded.v1alpha1.ConverseConfig;
import com.google.assistant.embedded.v1alpha1.ConverseRequest;
import com.google.assistant.embedded.v1alpha1.ConverseResponse;
import com.google.assistant.embedded.v1alpha1.ConverseState;
import com.google.assistant.embedded.v1alpha1.EmbeddedAssistantGrpc;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.OAuth2Credentials;
import com.google.protobuf.ByteString;
import com.mautini.assistant.demo.authentication.OAuthCredentials;
import com.mautini.assistant.demo.config.AssistantConf;
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

public class AssistantClient implements StreamObserver<ConverseResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssistantClient.class);

    private CountDownLatch finishLatch = new CountDownLatch(1);

    private EmbeddedAssistantGrpc.EmbeddedAssistantStub embeddedAssistantStub;

    private ByteArrayOutputStream currentResponse = new ByteArrayOutputStream();

    // See reference.conf
    private AssistantConf assistantConf;

    /**
     * Conversation state to continue a conversation if needed
     *
     * @see <a href="https://developers.google.com/assistant/sdk/reference/rpc/google.assistant.embedded.v1alpha1#google.assistant.embedded.v1alpha1.ConverseState">Google documentation</a>
     */
    private ByteString currentConversationState;

    public AssistantClient(OAuthCredentials oAuthCredentials, AssistantConf assistantConf) {
        this.assistantConf = assistantConf;

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
            StreamObserver<ConverseRequest> requester = embeddedAssistantStub.converse(this);
            requester.onNext(getConfigRequest());

            // Divide the audio request into chunks
            byte[][] chunks = divideArray(request, assistantConf.getChunkSize());

            // Send a request for each chunk
            for (byte[] chunk : chunks) {
                ByteString audioIn = ByteString.copyFrom(chunk);

                // Chunk of the request
                ConverseRequest converseRequest = ConverseRequest
                        .newBuilder()
                        .setAudioIn(audioIn)
                        .build();

                requester.onNext(converseRequest);
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
    public void onNext(ConverseResponse value) {
        try {
            if (value.getEventType() != null && value.getEventType() != ConverseResponse.EventType.EVENT_TYPE_UNSPECIFIED) {
                LOGGER.info("Event type : {}", value.getEventType().name());
            }

            if (value.getError() != null && value.getError().getCode() != 0) {
                LOGGER.info("Error in response : {}", value.getError().getMessage());
            }

            if (value.getAudioOut() != null) {
                currentResponse.write(value.getAudioOut().getAudioData().toByteArray());
            }

            if (value.getResult() != null) {
                currentConversationState = value.getResult().getConversationState();

                if (value.getResult().getSpokenRequestText() != null
                        && !value.getResult().getSpokenRequestText().isEmpty()) {

                    LOGGER.info("Request Text : {}", value.getResult().getSpokenRequestText());
                }

                if (value.getResult().getSpokenResponseText() != null
                        && !value.getResult().getSpokenResponseText().isEmpty()) {

                    LOGGER.info("Response Text : {}", value.getResult().getSpokenResponseText());
                }
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
    private ConverseRequest getConfigRequest() {
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

        ConverseState converseState = null;
        if (currentConversationState != null) {
            converseState = ConverseState
                    .newBuilder()
                    .setConversationState(currentConversationState)
                    .build();
        }

        ConverseConfig.Builder converseConfigBuilder = ConverseConfig
                .newBuilder()
                .setAudioInConfig(audioInConfig)
                .setAudioOutConfig(audioOutConfig);

        if (converseState != null) {
            converseConfigBuilder.setConverseState(converseState);
        }

        return ConverseRequest
                .newBuilder()
                .setConfig(converseConfigBuilder.build())
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
