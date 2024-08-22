package com.example.springOAuth.service.Apps;

import lombok.Data;

@Data
public class GoogleCredential {

    private String clientId;
    private String clientSecret;
    private String redirectUrl;
}
