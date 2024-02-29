package com.example.springOAuth.security.oauth2.user;

import java.util.Map;

import com.example.springOAuth.entity.IdentityProvider;
import com.example.springOAuth.exception.OAuth2AuthenticationProcessingException;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String providerName, Map<String, Object> attribute) {

        if (providerName.equalsIgnoreCase(IdentityProvider.GOOGLE.toString())) {
            return new GoogleOAuth2User(attribute);
        } else if (providerName.equalsIgnoreCase(IdentityProvider.GITHUB.toString())) {
            return new GithubOAuthUser(attribute);
        } else {
            throw new OAuth2AuthenticationProcessingException(
                    "Sorry! Login with " + providerName + " is not supported yet.");
        }

    }

}
