package com.mautini.assistant.demo.authentication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OAuthClient {

    @FormUrlEncoded
    @POST("token")
    Call<OAuthCredentials> getAccessToken(@Field("code") String code,
                                          @Field("client_id") String clientId,
                                          @Field("client_secret") String clientSecret,
                                          @Field("redirect_uri") String redirectUri,
                                          @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("token")
    Call<OAuthCredentials> refreshAccessToken(@Field("refresh_token") String refreshToken,
                                              @Field("client_id") String clientId,
                                              @Field("client_secret") String clientSecret,
                                              @Field("grant_type") String grantType);
}