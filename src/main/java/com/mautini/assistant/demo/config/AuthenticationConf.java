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

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCodeRedirectUri() {
        return codeRedirectUri;
    }

    public void setCodeRedirectUri(String codeRedirectUri) {
        this.codeRedirectUri = codeRedirectUri;
    }

    public String getGoogleOAuthEndpoint() {
        return googleOAuthEndpoint;
    }

    public void setGoogleOAuthEndpoint(String googleOAuthEndpoint) {
        this.googleOAuthEndpoint = googleOAuthEndpoint;
    }

    public String getUrlGoogleAccount() {
        return urlGoogleAccount;
    }

    public void setUrlGoogleAccount(String urlGoogleAccount) {
        this.urlGoogleAccount = urlGoogleAccount;
    }

    public String getCredentialsFilePath() {
        return credentialsFilePath;
    }

    public void setCredentialsFilePath(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
    }

    public Long getMaxDelayBeforeRefresh() {
        return maxDelayBeforeRefresh;
    }

    public void setMaxDelayBeforeRefresh(Long maxDelayBeforeRefresh) {
        this.maxDelayBeforeRefresh = maxDelayBeforeRefresh;
    }
}
