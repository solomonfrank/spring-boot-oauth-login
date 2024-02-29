package com.example.springOAuth.security.oauth2.user;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {

        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getEmail();

    public abstract String getName();

    public abstract String getImageUrl();

    public abstract boolean getEmailVerified();
}
