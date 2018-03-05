package com.mautini.assistant.demo;

import com.mautini.assistant.demo.api.AssistantClient;
import com.mautini.assistant.demo.authentication.AuthenticationHelper;
import com.mautini.assistant.demo.client.audio.AudioPlayer;
import com.mautini.assistant.demo.client.audio.AudioRecorder;
import com.mautini.assistant.demo.config.AssistantConf;
import com.mautini.assistant.demo.config.AudioConf;
import com.mautini.assistant.demo.config.AuthenticationConf;
import com.mautini.assistant.demo.config.DeviceRegisterConf;
import com.mautini.assistant.demo.device.DeviceRegister;
import com.mautini.assistant.demo.exception.AudioException;
import com.mautini.assistant.demo.exception.AuthenticationException;
import com.mautini.assistant.demo.exception.ConverseException;
import com.mautini.assistant.demo.exception.DeviceRegisterException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class GoogleAssistantClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleAssistantClient.class);

    public static void main(String[] args) throws AuthenticationException, AudioException, ConverseException, DeviceRegisterException {

        Config root = ConfigFactory.load();
        AuthenticationConf authenticationConf = ConfigBeanFactory.create(root.getConfig("authentication"), AuthenticationConf.class);
        DeviceRegisterConf deviceRegisterConf = ConfigBeanFactory.create(root.getConfig("deviceRegister"), DeviceRegisterConf.class);
        AssistantConf assistantConf = ConfigBeanFactory.create(root.getConfig("assistant"), AssistantConf.class);
        AudioConf audioConf = ConfigBeanFactory.create(root.getConfig("audio"), AudioConf.class);

        // Authentication
        AuthenticationHelper authenticationHelper = new AuthenticationHelper(authenticationConf);
        authenticationHelper
                .authenticate()
                .orElseThrow(() -> new AuthenticationException("Error during authentication"));

        // Check if we need to refresh the access token to request the api
        if (authenticationHelper.expired()) {
            authenticationHelper
                    .refreshAccessToken()
                    .orElseThrow(() -> new AuthenticationException("Error refreshing access token"));
        }

        // Register Device model and device
        DeviceRegister deviceRegister = new DeviceRegister(deviceRegisterConf, authenticationHelper.getOAuthCredentials().getAccessToken());
        deviceRegister.register();

        // Build the client (stub)
        AssistantClient assistantClient = new AssistantClient(authenticationHelper.getOAuthCredentials(), assistantConf,
                deviceRegister.getDeviceModel(), deviceRegister.getDevice());

        // Build the objects to record and play the conversation
        AudioRecorder audioRecorder = new AudioRecorder(audioConf);
        AudioPlayer audioPlayer = new AudioPlayer(audioConf);

        // Initiating Scanner to take user input
        Scanner scanner = new Scanner(System.in);



        // Main loop
        while (true) {
            // Check if we need to refresh the access token to request the api
            if (authenticationHelper.expired()) {
                authenticationHelper
                        .refreshAccessToken()
                        .orElseThrow(() -> new AuthenticationException("Error refreshing access token"));

                // Update the token for the assistant client
                assistantClient.updateCredentials(authenticationHelper.getOAuthCredentials());
            }


            // Text Input
            // Taking user input in
            String query = scanner.next();
            // Converting user into byte array to keep consistency of requestAssistant params
            byte[] queryByte = query.getBytes();

            // requesting assistant with text query
            byte[] response = assistantClient.requestAssistant(queryByte, "text");

            if (response.length > 0) {
                LOGGER.info(assistantClient.getStringResponse());
            } else {
                LOGGER.info("No response from the API");
            }


            // Audio Input: uncomment to run
            // Record
//            byte[] request = audioRecorder.getRecord();
//
//            // Request the api
//            byte[] response = assistantClient.requestAssistant(request, "audio");
//
//            // Play result if any
//            if (response.length > 0) {
//                audioPlayer.play(response);
//            } else {
//                LOGGER.info("No response from the API");
//            }
        }
    }
}
