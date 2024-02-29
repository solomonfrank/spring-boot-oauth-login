package com.example.springOAuth.security.oauth2;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.example.springOAuth.entity.IdentityProvider;
import com.example.springOAuth.entity.User;
import com.example.springOAuth.exception.OAuth2AuthenticationProcessingException;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.security.UserPrincipal;
import com.example.springOAuth.security.oauth2.user.OAuth2UserInfo;
import com.example.springOAuth.security.oauth2.user.OAuth2UserInfoFactory;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuthUserRequest) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(oAuthUserRequest);

        try {
            return processOAuth2User(oAuthUserRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the
            // OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }

    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuthUserRequest, OAuth2User oAuth2User) {

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuthUserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if (StringUtils.isBlank(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getIdentityProvider()
                    .equals(IdentityProvider
                            .valueOf(oAuthUserRequest.getClientRegistration().getRegistrationId().toUpperCase()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getIdentityProvider() + " account. Please use your " + user.getIdentityProvider() +
                        " account to login.");
            }

            // if (user.getIdentityProvider().name()
            // .equalsIgnoreCase(oAuthUserRequest.getClientRegistration().getRegistrationId()))
            // {
            // throw new OAuth2AuthenticationProcessingException("Looks like you're signed
            // up with " +
            // user.getIdentityProvider() + " account. Please use your " +
            // user.getIdentityProvider() +
            // " account to login.");
            // }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuthUserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user);
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder().email(oAuth2UserInfo.getEmail())
                .identityProvider(
                        IdentityProvider
                                .valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()))
                .name(oAuth2UserInfo.getName())
                .providerId(oAuth2UserInfo.getId())
                .imageUrl(oAuth2UserInfo.getImageUrl())
                .emailVerified(oAuth2UserInfo.getEmailVerified())
                .build();
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

    // public OAuth2User processOAuth2User(String registrationId, OAuth2User
    // oAuth2User) {
    // String userEmail = oAuth2User.getAttribute("email");

    // OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
    // registrationId, oAuth2User.getAttributes());

    // if (StringUtils.isBlank(oAuth2UserInfo.getEmail())) {
    // throw new OAuth2AuthenticationProcessingException("Email not found from
    // OAuth2 provider");
    // }

    // Optional<User> userOptional =
    // userRepository.findByEmail(oAuth2UserInfo.getEmail());

    // User user;

    // if (userOptional.isPresent()) {
    // user = userOptional.get();

    // var g = user.getIdentityProvider().toString();

    // if (user.getIdentityProvider().toString()
    // .equalsIgnoreCase(registrationId)) {
    // throw new OAuth2AuthenticationProcessingException("Looks like you're signed
    // up with " +
    // user.getIdentityProvider() + " account. Please use your " +
    // user.getIdentityProvider() +
    // " account to login.");
    // }
    // user = updateExistingUser(user, oAuth2UserInfo);
    // } else {
    // user = registerNewUser(registrationId, oAuth2UserInfo);
    // }

    // return UserPrincipal.create(user);
    // }

}
