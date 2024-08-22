package com.example.springOAuth.service.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.springOAuth.response.BookingResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private IEmailProvider emailStrategy;

    @Async
    public void sendEventBookRemainder(BookingResponse bookingResponse) {
        emailStrategy.sendEventBookRemainder(bookingResponse);
    }
}
