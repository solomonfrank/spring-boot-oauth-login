package com.example.springOAuth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @Email(message = "Invalid email format")
    @NotBlank
    private String email;

    @NotBlank
    private String amount;

    @NotNull
    private Long bookingId;
}
