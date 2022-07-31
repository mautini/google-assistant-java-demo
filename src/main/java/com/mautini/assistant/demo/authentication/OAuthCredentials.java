package com.mautini.assistant.demo.authentication;

import com.google.gson.annotations.SerializedName;

public class OAuthCredentials {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private int expiresIn;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("id_token")
    private String idToken;

    @SerializedName("expiration_time")
    private Long expirationTime;

    public OAuthCredentials() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @SuppressWarnings("unused")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @SuppressWarnings("unused")
    public String getIdToken() {
        return idToken;
    }

    @SuppressWarnings("unused")
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @SuppressWarnings("unused")
    public Long getExpirationTime() {
        return expirationTime;
    }

    @SuppressWarnings("unused")
    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }
}