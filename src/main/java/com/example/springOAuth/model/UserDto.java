package com.example.springOAuth.model;

import com.example.springOAuth.entity.IdentityProvider;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    @Schema(example = "john@example.com")
    private String email;
    private String name;
    private String imageUrl;
    private String providerId;
    private Boolean emailVerified;
    private IdentityProvider identityProvider;
}
