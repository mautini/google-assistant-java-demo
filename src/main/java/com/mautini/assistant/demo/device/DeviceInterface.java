package com.mautini.assistant.demo.device;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface for registering a device model and a device instance
 *
 * @see <a href="https://developers.google.com/assistant/sdk/reference/device-registration/register-device-manual">Google documentation (register)</a>
 * @see <a href="https://developers.google.com/assistant/sdk/reference/device-registration/model-and-instance-schemas#device_model_json">Google documentation (data model)</a>
 */
public interface DeviceInterface {

    @POST("v1alpha2/projects/{project_id}/deviceModels/")
    Call<DeviceModel> registerModel(@Path("project_id") String projectId, @Body DeviceModel deviceModel);

    @POST("v1alpha2/projects/{project_id}/devices/")
    Call<Device> registerDevice(@Path("project_id") String projectId, @Body Device device);
}