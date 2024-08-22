package com.example.springOAuth.service.Email;

import com.example.springOAuth.response.BookingResponse;

public interface IEmailProvider {

    void sendEventBookRemainder(BookingResponse bookingResponse);

    void sendEmailVerificationLink();

}
