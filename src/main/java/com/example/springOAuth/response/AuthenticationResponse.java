package com.example.springOAuth.response;

import com.example.springOAuth.model.UserDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9")
    private String token;
    private UserDto user;
}
