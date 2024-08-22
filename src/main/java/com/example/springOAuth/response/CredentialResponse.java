package com.example.springOAuth.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CredentialResponse {
    private Long id;

    private String slug;

    private LocalDateTime createdAt;

    private Long userId;
}
