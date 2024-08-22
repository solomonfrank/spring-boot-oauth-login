package com.example.springOAuth.config;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.springOAuth.exception.BadRequestException;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.security.JwtService;
import com.example.springOAuth.security.UserPrincipal;
import com.example.springOAuth.security.oauth2.CustomOAuth2UserService;
import com.example.springOAuth.util.CookieUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();

        // var provider = oauthToken.getAuthorizedClientRegistrationId();

        // System.out.println("*****" + oauthUser);
        // String email = oauthUser.getAttribute("email");
        // String username = oauthUser.getAttribute("login");

        // String picture = oauthUser.getAttribute("picture");

        // String registrationId = oauthToken.getAuthorizedClientRegistrationId();

        // OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
        // registrationId, oauthUser.getAttributes());

        // try {
        // customOAuth2UserService.processOAuth2User(registrationId, oauthUser);
        // } catch (AuthenticationException ex) {
        // throw ex;
        // } catch (Exception ex) {
        // // Throwing an instance of AuthenticationException will trigger the
        // // OAuth2AuthenticationFailureHandler
        // throw new InternalAuthenticationServiceException(ex.getMessage(),
        // ex.getCause());
        // }

        // String targetUrl = determineTargetUrl(request, response, authentication);

        // System.out.println("*****" + email + " " + username + " " + registrationId +
        // " " + targetUrl + " " + provider + " " + picture + " " + oAuth2UserInfo);

        // getRedirectStrategy().sendRedirect(request, response, targetUrl);

        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {

        Optional<String> redirectUri = CookieUtils.getCookie(request, "redirect_uri").map(Cookie::getValue);
        // REDIRECT_URI_PARAM_COOKIE_NAME)
        // .map(Cookie::getValue);
        var userPrincipal = (UserPrincipal) authentication.getPrincipal();

        var isAuthorizedPath = isAuthorizedUrl(redirectUri.get());

        if (redirectUri.isPresent() && !isAuthorizedPath) {
            throw new BadRequestException(
                    "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        var email = userPrincipal.getEmail();

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }

    private boolean isAuthorizedUrl(String uri) {
        URI clientRedirecturi = URI.create(uri);
        return appProperties.getOauth2().getAuthorizedUri().stream().anyMatch(oauthuri -> {

            var authorizedUri = URI.create(oauthuri);

            if (authorizedUri.getHost().equalsIgnoreCase(clientRedirecturi.getHost())
                    && authorizedUri.getPort() == clientRedirecturi.getPort()) {
                return true;
            }
            return false;
        });

    }

}
