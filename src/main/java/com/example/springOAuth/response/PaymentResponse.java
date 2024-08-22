package com.example.springOAuth.response;

import com.example.springOAuth.entity.PaymentProvider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private String reference;
    private String amount;
    private boolean status;
    private PaymentProvider paymentProvider;
    private String authorizationUrl;
    private String accessCode;
}
