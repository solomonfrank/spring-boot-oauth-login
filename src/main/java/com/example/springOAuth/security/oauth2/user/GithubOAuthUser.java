package com.example.springOAuth.security.oauth2.user;

import java.util.Map;

public class GithubOAuthUser extends OAuth2UserInfo {

    public GithubOAuthUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    @Override
    public String getEmail() {

        return (String) attributes.get("email");

    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }

    @Override
    public boolean getEmailVerified() {
        return (Boolean) attributes.get("email_verified");
    }

}
