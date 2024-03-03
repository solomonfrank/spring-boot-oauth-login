package com.example.springOAuth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttendeeRequest {
    @Email
    @NotBlank(message = "Email address is required.")
    private String email;

    @NotBlank(message = "Name is required.")
    private String name;

    private String timeZone;
}
