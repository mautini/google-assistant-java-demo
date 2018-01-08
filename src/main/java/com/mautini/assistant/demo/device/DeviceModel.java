package com.mautini.assistant.demo.device;

public class DeviceModel {

    private String deviceModelId;

    private String projectId;

    private Manifest manifest;

    private String name;

    private String deviceType;

    public DeviceModel() {
    }

    public String getDeviceModelId() {
        return deviceModelId;
    }

    public void setDeviceModelId(String deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public void setManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public static class Manifest {

        private String manufacturer;

        private String productName;

        private String deviceDescription;

        public Manifest() {
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getDeviceDescription() {
            return deviceDescription;
        }

        public void setDeviceDescription(String deviceDescription) {
            this.deviceDescription = deviceDescription;
        }
    }
}