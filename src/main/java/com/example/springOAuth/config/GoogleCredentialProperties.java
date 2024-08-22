package com.example.springOAuth.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@ConfigurationProperties("google")
@Configuration
@Data
public class GoogleCredentialProperties {

    private CredentialDetails credentials = new CredentialDetails();

    @Data
    public static class CredentialDetails {

        private String client_id;
        private String client_secret;
        private String auth_uri;
        private String token_uri;
        private String auth_provider_x509_cert_url;
        private List<String> redirect_uris = new ArrayList<>();

        private String redirectUrl;

    }

}
