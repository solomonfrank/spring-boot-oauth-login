package com.example.springOAuth.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.springOAuth.util.CookieUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
        @Autowired
        HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException exception) throws IOException, ServletException {
                String targetUrl = CookieUtils
                                .getCookie(request,
                                                HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                                .map(Cookie::getValue)
                                .orElse(("/"));

                targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                                .queryParam("error", exception.getMessage().toString())
                                .build().toUriString();

                httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

                getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
}
