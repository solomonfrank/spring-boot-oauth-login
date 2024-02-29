package com.example.springOAuth.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.springOAuth.entity.VerificationToken;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.repository.VerificationTokenRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenService {
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Async
    public void sendEmailVerification(String email, String targetUrl) throws MessagingException {
        var token = UUID.randomUUID().toString();
        var verificationToken = VerificationToken.builder().identifier(email).token(token)
                .expires(ZonedDateTime.now().plusDays(1))
                .build();

        verificationTokenRepository.save(verificationToken);

        var url = UriComponentsBuilder.fromUriString(targetUrl)
                .path("/api/auth/verify-email")
                .queryParam("token", token).build().toUriString();
        log.info("Sending email verification link");
        emailService.sendEmailVerificationLink(email, url);
        log.info("Email sent successfully");

    }

    public void verifiyEmailHandler(String token) {

        VerificationToken foundToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("Token not found"));

        if (foundToken.getExpires().isBefore(ZonedDateTime.now())) {
            throw new UsernameNotFoundException("Token expired");
        }

        var user = userRepository.findByEmail(foundToken.getIdentifier())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEmailVerified(true);
        userRepository.save(user);

        verificationTokenRepository.deleteById(foundToken.getId()); // Delete token from DB after it has been used

    }
}
