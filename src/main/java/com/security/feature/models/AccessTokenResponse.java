package com.security.feature.models;

public class AccessTokenResponse {
    private String accessToken;

    public AccessTokenResponse(String accessToken){
        this.accessToken = accessToken;
    }

    public String getAccessToken(){
        return accessToken;
    }
}
