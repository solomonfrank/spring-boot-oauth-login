package com.example.springOAuth.service.payment;

import com.example.springOAuth.model.PaymentRequest;
import com.example.springOAuth.response.PaymentResponse;

public class FlutterwaveStrategy implements IPaymentStrategy {

    @Override
    public void makePayment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'makePayment'");
    }

    @Override
    public void verifyPayment(String reference) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyPayment'");
    }

    @Override
    public PaymentResponse initializePayment(PaymentRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initializePayment'");

    }

}
