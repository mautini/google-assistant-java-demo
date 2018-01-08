package com.mautini.assistant.demo.device;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mautini.assistant.demo.config.DeviceRegisterConf;
import com.mautini.assistant.demo.exception.DeviceRegisterException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class DeviceRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRegister.class);

    // Configuration from typesafe
    private DeviceRegisterConf deviceRegisterConf;

    private DeviceModel deviceModel;

    private Device device;

    // The API interface (used by retrofit)
    private DeviceInterface deviceInterface;

    // The Gson object to read/write the device model and instance in a file
    private Gson gson;

    public DeviceRegister(DeviceRegisterConf deviceRegisterConf, String accessToken) {
        this.deviceRegisterConf = deviceRegisterConf;

        gson = new Gson();

        // Add an interceptor to add our accessToken in the queries
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            return chain.proceed(newRequest);
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(deviceRegisterConf.getApiEndpoint())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        deviceInterface = retrofit.create(DeviceInterface.class);
    }

    public void register() throws DeviceRegisterException {
        String projectId = deviceRegisterConf.getProjectId();
        // Register the device model
        deviceModel = registerModel(projectId)
                .orElseThrow(() -> new DeviceRegisterException("Unable to register the device model"));
        // Now we can register the instance
        device = registerInstance(projectId, deviceModel.getDeviceModelId())
                .orElseThrow(() -> new DeviceRegisterException("Unable to register the device instance"));
    }

    public DeviceModel getDeviceModel() {
        return deviceModel;
    }

    public Device getDevice() {
        return device;
    }

    private Optional<DeviceModel> registerModel(String projectId) throws DeviceRegisterException {
        Optional<DeviceModel> optionalDeviceModel = readFromFile(deviceRegisterConf.getDeviceModelFilePath(), DeviceModel.class);
        if (optionalDeviceModel.isPresent()) {
            LOGGER.info("Got device model from file");
            return optionalDeviceModel;
        }

        // If we can't get the device model from a file, continue with the webservice
        String modelId = projectId + UUID.randomUUID();

        DeviceModel.Manifest manifest = new DeviceModel.Manifest();
        manifest.setManufacturer("mautini");
        manifest.setProductName("Assistant SDK Demo");
        manifest.setDeviceDescription("Assistant SDK Demo in Java");

        DeviceModel deviceModel = new DeviceModel();
        deviceModel.setDeviceModelId(modelId);
        deviceModel.setProjectId(projectId);
        deviceModel.setName(String.format("projects/%s/deviceModels/%s", projectId, modelId));
        // https://developers.google.com/assistant/sdk/reference/device-registration/model-and-instance-schemas#device_model_json
        // Light does not fit this project but there is nothing better in the API
        deviceModel.setDeviceType("action.devices.types.LIGHT");
        deviceModel.setManifest(manifest);

        try {
            LOGGER.info("Creating device model");
            Response<DeviceModel> response = deviceInterface.registerModel(projectId, deviceModel).execute();
            if (response.isSuccessful() && response.body() != null) {
                // Save the device model in a file to not request the api each time we start the project
                try (FileWriter writer = new FileWriter(deviceRegisterConf.getDeviceModelFilePath())) {
                    gson.toJson(response.body(), writer);
                }
                return Optional.of(response.body());
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new DeviceRegisterException("Error during registration of the device model", e);
        }
    }

    private Optional<Device> registerInstance(String projectId, String modelId) throws DeviceRegisterException {
        Optional<Device> optionalDevice = readFromFile(deviceRegisterConf.getDeviceInstanceFilePath(), Device.class);
        if (optionalDevice.isPresent()) {
            LOGGER.info("Got device instance from file");
            return optionalDevice;
        }

        Device device = new Device();
        device.setId(UUID.randomUUID().toString());
        device.setModelId(modelId);
        // Here we use the Google Assistant Service
        device.setClientType("SDK_SERVICE");

        try {
            LOGGER.info("Creating device instance");
            Response<Device> response = deviceInterface.registerDevice(projectId, device).execute();
            if (response.isSuccessful() && response.body() != null) {
                // Save the device instance in a file to not request the api each time we start the project
                try (FileWriter writer = new FileWriter(deviceRegisterConf.getDeviceInstanceFilePath())) {
                    gson.toJson(response.body(), writer);
                }
                return Optional.of(response.body());
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new DeviceRegisterException("Error during registration of the device instance", e);
        }
    }

    /**
     * Deserialize from json an object in a file
     *
     * @param filePath    the file in which we stored the object to deserialize
     * @param targetClass the target class for the deserialization
     * @param <T>         the type of the object to deserialize
     * @return an optional with the object deserialized if the process succeed
     */
    private <T> Optional<T> readFromFile(String filePath, Class<T> targetClass) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                return Optional.of(gson.fromJson(new JsonReader(new FileReader(file)), targetClass));
            } catch (IOException e) {
                LOGGER.warn("Unable to read the content of the file", e);
            }
        }
        return Optional.empty();
    }
}