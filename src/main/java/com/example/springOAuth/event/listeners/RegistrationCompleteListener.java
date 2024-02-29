package com.example.springOAuth.event.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.springOAuth.event.RegistrationCompleteEvent;
import com.example.springOAuth.service.VerificationTokenService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RegistrationCompleteListener {

    @Autowired
    private VerificationTokenService verificationTokenService;

    // class should implement implements
    // ApplicationListener<RegistrationCompleteEvent>
    // @Override
    // public void onApplicationEvent(RegistrationCompleteEvent event) {

    // try {
    // verificationTokenService.sendEmailVerification(event.getEmail(),
    // event.getApplicationUrl());
    // } catch (Exception ex) {
    // log.error("Email error", ex);
    // }

    // }

    @EventListener
    @Async
    public void sendEmailVerificationListener(RegistrationCompleteEvent event) {
        try {
            verificationTokenService.sendEmailVerification(event.getEmail(), event.getApplicationUrl());
        } catch (Exception ex) {
            log.error("Email error", ex);
        }
    }
}
