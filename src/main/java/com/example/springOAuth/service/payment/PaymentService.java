package com.example.springOAuth.service.payment;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.springOAuth.entity.Payment;
import com.example.springOAuth.entity.PaymentStatus;
import com.example.springOAuth.exception.ResourceNotFoundException;
import com.example.springOAuth.model.PaymentRequest;
import com.example.springOAuth.repository.BookingRepository;
import com.example.springOAuth.repository.PaymentRepository;
import com.example.springOAuth.response.PaymentResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IPaymentStrategy paymentStrategy;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentResponse initializePayment(PaymentRequest request) {
        var response = paymentStrategy.initializePayment(request);

        var booking = bookingRepository.findById(request.getBookingId()).orElseThrow(
                () -> new ResourceNotFoundException("Booking with id" + request.getBookingId() + " does not exist"));

        var payment = Payment.builder()
                .amount(new BigDecimal(response.getAmount()))
                .paymentStatus(PaymentStatus.SUCCESS)
                .paymentProvider(response.getPaymentProvider())
                .reference(response.getReference())
                .booking(booking)
                .build();

        paymentRepository.save(payment);

        return response;
    }

    public void handlePayment() {
        paymentStrategy.makePayment();
    }

    public void verifyTransaction(String reference) {

        paymentStrategy.verifyPayment(reference);
    }

}
