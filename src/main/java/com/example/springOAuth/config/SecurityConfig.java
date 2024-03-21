package com.example.springOAuth.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.springOAuth.security.JwtConfigurationFilter;
import com.example.springOAuth.security.oauth2.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        @Autowired
        private OAuth2SuccessHandler OAuth2SuccessHandler;

        private final CustomOAuth2UserService customOAuth2UserService;

        @Autowired
        private OAuth2FailureHandler oAuth2AuthenticationFailureHandler;

        private final JwtConfigurationFilter jwtFilter;

        private final LogoutHandler logoutHandler;

        @Value("${app.cors.allowedOrigins}")
        private String allowedOrigins;

        /*
         * By default, Spring OAuth2 uses
         * HttpSessionOAuth2AuthorizationRequestRepository to save
         * the authorization request. But, since our service is stateless, we can't save
         * it in
         * the session. We'll save the request in a Base64 encoded cookie instead.
         */
        @Bean
        public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
                return new HttpCookieOAuth2AuthorizationRequestRepository();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                return http
                                .cors(cors -> cors.configurationSource(corsConfiguration()))
                                .csrf(csrf -> csrf.disable())
                                // .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())

                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/oauth2/**").permitAll()
                                                .requestMatchers("/login/**").permitAll()

                                                .requestMatchers(HttpMethod.GET, "/api/v1/event-type/{userId}/{slug}")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/v1/booking").permitAll()
                                                .requestMatchers("/", "/error", "/csrf", "/swagger-ui.html",
                                                                "/swagger-ui/**", "/v3/api-docs",
                                                                "/v3/api-docs/**")
                                                .permitAll()
                                                .anyRequest().authenticated()

                                )

                                .oauth2Login(oauth2 -> oauth2
                                                .authorizationEndpoint(auth -> auth.baseUri("/oauth2/authorize")
                                                                .authorizationRequestRepository(this
                                                                                .cookieAuthorizationRequestRepository()))
                                                .redirectionEndpoint(redirection -> redirection
                                                                .baseUri("/login/oauth2/code/*"))
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService))
                                                .successHandler(OAuth2SuccessHandler)
                                                .failureHandler(oAuth2AuthenticationFailureHandler))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .formLogin(c -> c.disable())

                                .logout(l -> l.logoutUrl("/api/v1/auth/logout").addLogoutHandler(logoutHandler)
                                                .logoutSuccessHandler(
                                                                (request, response,
                                                                                authentication) -> SecurityContextHolder
                                                                                                .clearContext())

                                                .logoutSuccessUrl("/").permitAll())
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(
                                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                                // .authenticationEntryPoint(new RestAuthenticationEntrypoint())
                                .build();

        }

        @Bean
        CorsConfigurationSource corsConfiguration() {
                CorsConfiguration config = new CorsConfiguration();

                config.setAllowedOrigins(List.of(allowedOrigins));
                config.addAllowedHeader("*");
                config.addAllowedMethod("*");
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
                urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", config);

                return urlBasedCorsConfigurationSource;
        }

        @Bean

        public WebMvcConfigurer webMvcConfigurer() {

                return new WebMvcConfigurer() {
                        public void addCorsMappings(CorsRegistry registry) {
                                registry.addMapping("/**")
                                                .allowedOrigins("")
                                                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                                                .allowedHeaders("*")
                                                .allowCredentials(true)
                                                .maxAge(800);
                        }

                };

        }

}
