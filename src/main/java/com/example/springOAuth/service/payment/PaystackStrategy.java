package com.example.springOAuth.service.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.springOAuth.entity.PaymentProvider;
import com.example.springOAuth.entity.PaymentStatus;
import com.example.springOAuth.exception.ResourceNotFoundException;
import com.example.springOAuth.feignClient.PaystackClient;
import com.example.springOAuth.model.PaymentRequest;
import com.example.springOAuth.repository.PaymentRepository;
import com.example.springOAuth.response.PaymentResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaystackStrategy implements IPaymentStrategy {

    private final PaystackClient paystackClient;

    private final PaymentRepository paymentRepository;

    @Value("${app.payment.paystack.secretkey}")
    private String secrekKey;

    @Override
    public void makePayment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'makePayment'");
    }

    @Override
    public PaymentResponse initializePayment(PaymentRequest request) {
        var response = paystackClient.initializeTransaction("Bearer " + secrekKey, request)
                .getBody();
        var paymentResponse = PaymentResponse.builder().amount(request.getAmount())
                .paymentProvider(PaymentProvider.PAYSTACK)
                .authorizationUrl(response.getData().getAuthorization_url())
                .reference(response.getData().getReference()).status(response.isStatus()).build();

        System.out.println(response);
        return paymentResponse;
    }

    @Override
    public void verifyPayment(String reference) {

        try {
            log.info("paystck request initiated");
            var response = paystackClient.verifyTransaction("Bearer " + secrekKey, reference)
                    .getBody();

            log.info("paystck request completed with response" + response);

            if (response.getStatus()) {

                log.info("Retrieve db record");
                var existingPayment = paymentRepository.findByReference(response.getData().getReference())
                        .orElseThrow(() -> new ResourceNotFoundException("Transaction does not exist"));

                existingPayment.setPaymentStatus(PaymentStatus.SUCCESS);
                existingPayment.setCurrency(response.getData().getCurrency());

                paymentRepository.save(existingPayment);

            } else {
                throw new RuntimeException("Unable to process request");
            }

        } catch (Exception ex) {

            log.error("Failed to make request");
            throw new RuntimeException(ex);
        }

    }

}
