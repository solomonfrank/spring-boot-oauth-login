package com.example.springOAuth.model;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Length(min = 3, message = "Password must be more than three characters")
    private String password;
    @NotBlank
    private String name;

    private String username;

}
