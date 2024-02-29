package com.example.springOAuth.security;

import org.springframework.security.oauth2.client.endpoint.JwtBearerGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

public class TokenAuthentication implements OAuth2AccessTokenResponseClient<JwtBearerGrantRequest> {

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(JwtBearerGrantRequest authorizationGrantRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTokenResponse'");
    }

}
