package com.example.springOAuth.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@ConfigurationProperties("app")
@Configuration
@Data
public class AppProperties {

    private OAuth oauth2 = new OAuth();
    private Auth auth = new Auth();

    @Data
    public static class OAuth {

        private List<String> authorizedUri = new ArrayList<>();

    }

    @Data
    public static class Auth {
        private String tokenExpirationMsec;
        private List<String> allowedOrigins = new ArrayList<>();
    }

}
