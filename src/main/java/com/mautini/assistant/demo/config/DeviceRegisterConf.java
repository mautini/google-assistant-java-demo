package com.mautini.assistant.demo.config;

public class DeviceRegisterConf {

    private String apiEndpoint;

    private String projectId;

    private String deviceModelFilePath;

    private String deviceInstanceFilePath;

    public DeviceRegisterConf() {
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDeviceModelFilePath() {
        return deviceModelFilePath;
    }

    public void setDeviceModelFilePath(String deviceModelFilePath) {
        this.deviceModelFilePath = deviceModelFilePath;
    }

    public String getDeviceInstanceFilePath() {
        return deviceInstanceFilePath;
    }

    public void setDeviceInstanceFilePath(String deviceInstanceFilePath) {
        this.deviceInstanceFilePath = deviceInstanceFilePath;
    }
}
