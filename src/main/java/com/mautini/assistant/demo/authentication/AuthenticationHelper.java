package com.mautini.assistant.demo.authentication;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mautini.assistant.demo.config.AuthenticationConf;
import com.mautini.assistant.demo.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class AuthenticationHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHelper.class);

    // The current credentials for the app
    private OAuthCredentials oAuthCredentials;

    // The client to perform HTTP request for oAuth2 authentication
    private OAuthClient oAuthClient;

    // The Gson object to store the credentials in a file
    private Gson gson;

    // The configuration for the authentication module (see reference.conf in resources)
    private AuthenticationConf authenticationConf;

    public AuthenticationHelper(AuthenticationConf authenticationConf) {
        this.authenticationConf = authenticationConf;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(authenticationConf.getGoogleOAuthEndpoint())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        oAuthClient = retrofit.create(OAuthClient.class);
        gson = new Gson();
    }

    public OAuthCredentials getOAuthCredentials() {
        return oAuthCredentials;
    }

    public Optional<OAuthCredentials> authenticate() throws AuthenticationException {
        try {
            File file = new File(authenticationConf.getCredentialsFilePath());
            if (file.exists()) {
                LOGGER.info("Loading oAuth credentials from file");
                // If we have previous credentials in a file, use them
                oAuthCredentials = gson.fromJson(new JsonReader(new FileReader(authenticationConf.getCredentialsFilePath())), OAuthCredentials.class);
                LOGGER.info("Access Token: " + oAuthCredentials.getAccessToken());
            } else {
                // Create new credentials
                Optional<OAuthCredentials> optCredentials = requestAccessToken();
                if (optCredentials.isPresent()) {
                    oAuthCredentials = optCredentials.get();
                    LOGGER.info("Access Token: " + oAuthCredentials.getAccessToken());
                    saveCredentials();
                }
            }
            return Optional.of(oAuthCredentials);
        } catch (Exception e) {
            throw new AuthenticationException("Error during authentication", e);
        }
    }

    /**
     * Check if the token is expired
     *
     * @return true if the access token need to be refreshed false otherwise
     */
    public boolean expired() {
        // Add a delay to be sure to not make a request with an expired token
        return oAuthCredentials.getExpirationTime() - System.currentTimeMillis() < authenticationConf.getMaxDelayBeforeRefresh();
    }

    /**
     * Refresh the access token for oAuth authentication
     *
     * @return the new (refreshed) credentials
     */
    public Optional<OAuthCredentials> refreshAccessToken() throws AuthenticationException {
        LOGGER.info("Refreshing access token");
        try {
            Response<OAuthCredentials> response = oAuthClient.refreshAccessToken(
                    oAuthCredentials.getRefreshToken(),
                    authenticationConf.getClientId(),
                    authenticationConf.getClientSecret(),
                    "refresh_token")
                    .execute();

            if (response.isSuccessful()) {
                LOGGER.info("New Access Token: " + response.body().getAccessToken());
                oAuthCredentials.setAccessToken(response.body().getAccessToken());
                oAuthCredentials.setExpiresIn(response.body().getExpiresIn());
                oAuthCredentials.setTokenType(response.body().getTokenType());
                saveCredentials();
                return Optional.of(oAuthCredentials);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new AuthenticationException("Error during authentication", e);
        }
    }

    /**
     * Request an access token by asking the user to authorize the application
     *
     * @return credentials if the request succeeds
     * @throws URISyntaxException if the request fails
     * @throws IOException        if the request fails
     */
    private Optional<OAuthCredentials> requestAccessToken() throws URISyntaxException, IOException {
        String url = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "scope=" + authenticationConf.getScope() + "&" +
                "response_type=code&" +
                "redirect_uri=" + authenticationConf.getCodeRedirectUri() + "&" +
                "client_id=" + authenticationConf.getClientId();

        // Open a browser to authenticate using oAuth2
        Desktop.getDesktop().browse(new URI(url));

        LOGGER.info("Allow the application in your browser and copy the authorization code in the console");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine();

        Response<OAuthCredentials> response = oAuthClient.getAccessToken(
                code,
                authenticationConf.getClientId(),
                authenticationConf.getClientSecret(),
                authenticationConf.getCodeRedirectUri(),
                "authorization_code")
                .execute();

        if (response.isSuccessful()) {
            oAuthCredentials = response.body();
            return Optional.of(oAuthCredentials);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Save the credentials in a file
     *
     * @throws IOException if the file cannot be created
     */
    private void saveCredentials() throws IOException {
        try (FileWriter writer = new FileWriter(authenticationConf.getCredentialsFilePath())) {
            // Set the expiration Date
            oAuthCredentials.setExpirationTime(System.currentTimeMillis() + oAuthCredentials.getExpiresIn() * 1000);
            gson.toJson(oAuthCredentials, writer);
        }
    }
}
