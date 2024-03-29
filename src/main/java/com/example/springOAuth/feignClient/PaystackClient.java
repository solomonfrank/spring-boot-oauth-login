package com.example.springOAuth.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.springOAuth.model.PaymentRequest;
import com.example.springOAuth.response.PaystackTransactionInit;
import com.example.springOAuth.response.PaystackVerifyTransaction;

@FeignClient(name = "address-service", url = "${app.payment.paystack.baseUrl}")
public interface PaystackClient {
    @PostMapping(value = "/transaction/initialize", consumes = { "application/json" })
    ResponseEntity<PaystackTransactionInit> initializeTransaction(@RequestHeader("Authorization") String secretKey,
            @RequestBody PaymentRequest entity);

    @GetMapping(value = "/transaction/verify/{reference}", consumes = { "application/json" })
    ResponseEntity<PaystackVerifyTransaction> verifyTransaction(@RequestHeader("Authorization") String secretKey,
            @PathVariable("reference") String reference);
}
