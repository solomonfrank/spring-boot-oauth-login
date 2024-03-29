package com.example.springOAuth.service.payment;

import com.example.springOAuth.model.PaymentRequest;
import com.example.springOAuth.response.PaymentResponse;

public interface IPaymentStrategy {

    void makePayment();

    PaymentResponse initializePayment(PaymentRequest request);

    void verifyPayment(String reference);

}
