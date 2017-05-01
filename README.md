# Assistant-Java

## Goal

A simple client - demo project  for the [Google Assistant SDK](https://developers.google.com/assistant/sdk/).

This project was built and tested on a Mac.

## Configuration

The project use TypeSafe config for the configuration. The main configuration file is `src/main/resources/reference.conf`

The project use slf4j and log4j2 for logging. You can configure the logger with the `log4j2.xml` file in the resources folder.

To authenticate the requests to API, the Google Assistant SDK use the oAuth2 protocol.
As specified on [this page](https://developers.google.com/assistant/sdk/prototype/getting-started-other-platforms/config-dev-project-and-account),
you must generate a client Id and a client secret for the application.
Next you must provide theses values (id and secret) in the `reference.conf` file.

The project use generated bindings (package `com.google.assistant.embedded.v1alpha1`) from Google Apis (repository in References / Links section). If you want to update theses files,
you can follow [this guide](https://developers.google.com/assistant/sdk/prototype/getting-started-other-platforms/integrate#generate_the_grpc_bindings_for_your_language)

## Usage

The first time you start the application, you must authorize the application via the oAuth2 protocol.
You must copy and paste the authorization code from your browser in your terminal.

Next you can talk and hit enter to send a request to the API. You will receive a response and send another request if you want.

The application will store your oAuth2 credentials in a file so you will not have to authorize the application each time your run it.

## References / Links

- [Google assistant SDK](https://developers.google.com/assistant/sdk/)
- [Google Apis repository](https://github.com/googleapis/googleapis)
- [gRPC Documentation](http://www.grpc.io/docs/)
- [OAuth2 Documentation](https://developers.google.com/identity/protocols/OAuth2InstalledApp)
- [Python Sample](https://github.com/googlesamples/assistant-sdk-python)

Feel free to give me some feedback or ask questions if you have any.
