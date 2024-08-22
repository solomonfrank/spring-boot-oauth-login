package com.example.springOAuth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springOAuth.model.PaymentRequest;
import com.example.springOAuth.response.PaymentResponse;
import com.example.springOAuth.service.payment.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/initialize")
    public ResponseEntity<PaymentResponse> initializePayment(@Valid @RequestBody PaymentRequest entity) {
        PaymentResponse response = paymentService.initializePayment(entity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/verify/{reference}")
    public ResponseEntity<?> verifyTransaction(@PathVariable("reference") String reference) {
        paymentService.verifyTransaction(reference);
        Map<String, String> response = new HashMap<>();

        response.put("status", "succes");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
