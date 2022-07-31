package com.mautini.assistant.demo.config;

public class AuthenticationConf {

    private String clientId;

    private String clientSecret;

    private String scope;

    private String codeRedirectUri;

    private String googleOAuthEndpoint;

    private String urlGoogleAccount;

    private String credentialsFilePath;

    private Long maxDelayBeforeRefresh;

    public AuthenticationConf() {
    }

    public String getClientId() {
        return clientId;
    }

    @SuppressWarnings("unused")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    @SuppressWarnings("unused")
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    @SuppressWarnings("unused")
    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCodeRedirectUri() {
        return codeRedirectUri;
    }

    @SuppressWarnings("unused")
    public void setCodeRedirectUri(String codeRedirectUri) {
        this.codeRedirectUri = codeRedirectUri;
    }

    public String getGoogleOAuthEndpoint() {
        return googleOAuthEndpoint;
    }

    @SuppressWarnings("unused")
    public void setGoogleOAuthEndpoint(String googleOAuthEndpoint) {
        this.googleOAuthEndpoint = googleOAuthEndpoint;
    }

    @SuppressWarnings("unused")
    public String getUrlGoogleAccount() {
        return urlGoogleAccount;
    }

    @SuppressWarnings("unused")
    public void setUrlGoogleAccount(String urlGoogleAccount) {
        this.urlGoogleAccount = urlGoogleAccount;
    }

    public String getCredentialsFilePath() {
        return credentialsFilePath;
    }

    @SuppressWarnings("unused")
    public void setCredentialsFilePath(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
    }

    public Long getMaxDelayBeforeRefresh() {
        return maxDelayBeforeRefresh;
    }

    @SuppressWarnings("unused")
    public void setMaxDelayBeforeRefresh(Long maxDelayBeforeRefresh) {
        this.maxDelayBeforeRefresh = maxDelayBeforeRefresh;
    }
}
