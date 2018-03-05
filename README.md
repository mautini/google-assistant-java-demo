# Assistant-Java

## Goal

A simple client - demo project  for the [Google Assistant SDK](https://developers.google.com/assistant/sdk/).

This project was built and tested on a Mac.

## Version
**This README file is written for the latest version. Checkout the project at the desired version if you want to get the
associated README.** 

- v0.2.0 : Released on 09/01/2018 : Works with the Google Assistant SDK alpha2. This version allows you to use the Assistant SDK with the new GRPC binding but doesn't implements the new features from the alpha2 yet (as Device Actions). 
- v0.1.0 : Released on 01/05/2017 : Works with the Google Assistant SDK alpha1

## Configuration

The project use TypeSafe config for the configuration. The main configuration file is `src/main/resources/reference.conf`

The project use slf4j and log4j2 for logging. You can configure the logger with the `log4j2.xml` file in the resources folder.

To authenticate the requests to API, the Google Assistant SDK use the oAuth2 protocol.
As specified on [this page](https://developers.google.com/assistant/sdk/prototype/getting-started-other-platforms/config-dev-project-and-account),
you must generate a client Id and a client secret for the application.
Next you must provide theses values (id and secret) in the `reference.conf` file.

Since the alpha2 version of the SDK, the API must register a device model and a device instance to use the SDK. See the documentation for more information [here](https://developers.google.com/assistant/sdk/reference/device-registration/register-device-manual). The API handles this registration but you must provide in the `reference.conf` the id of the project you created during the previous step (to get your oAuth2 credentials). Set this value in the projectId field under deviceRegister in the `reference.conf` file.


The project use generated bindings (package `com.google.assistant.embedded.v1alpha2`) from Google Apis (repository in References / Links section). If you want to update theses files,
you can follow [this guide](https://developers.google.com/assistant/sdk/prototype/getting-started-other-platforms/integrate#generate_the_grpc_bindings_for_your_language)

### Audio / Text - Input / Output
In the `reference.conf` file, you can choose if you want to make your request with your voice or by entering text
(parameter `inputMode`). You can also choose if you want to play the response in audio with the `outputAudio` parameter.

## Usage

The first time you start the application, you must authorize the application via the oAuth2 protocol.
You must copy and paste the authorization code from your browser in your terminal.

The application will register a device model and a device instance with your project id (see the configuration part if you haven't set your project id). This information will be persisted in the `device_model.json` and `device_instance.json` files.

Next you can talk and hit enter to send a request to the API. You will receive a response and send another request if you want.

The application will store your oAuth2 credentials in a file so you will not have to authorize the application each time your run it.

## References / Links

- [Google assistant SDK](https://developers.google.com/assistant/sdk/)
- [Google Apis repository](https://github.com/googleapis/googleapis)
- [gRPC Documentation](http://www.grpc.io/docs/)
- [OAuth2 Documentation](https://developers.google.com/identity/protocols/OAuth2InstalledApp)
- [Python Sample](https://github.com/googlesamples/assistant-sdk-python)

Feel free to give me some feedback or ask questions if you have any.

## Youtube Demo Video
<a href="http://www.youtube.com/watch?feature=player_embedded&v=vTgU9PpZwso" target="_blank"><img src="http://img.youtube.com/vi/vTgU9PpZwso/0.jpg" alt="Youtube Video Demo" width="240" height="180" border="10" /></a>
