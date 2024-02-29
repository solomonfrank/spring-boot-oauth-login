package com.example.springOAuth.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendMail(String to, String subject, String body) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true indicates HTML format
        helper.setFrom("noreply@springaouth.com");

        javaMailSender.send(message);
    }

    public void sendEmailVerificationLink(String to, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Email Verification");
        // helper.setText(body);
        helper.setFrom("noreply@springaouth.com");

        Context context = new Context();
        context.setVariable("name", to);
        context.setVariable("body", body);
        String emailContent = templateEngine.process("email-template", context);

        helper.setText(emailContent, true);

        javaMailSender.send(message);
    }

    public void sendTemplateEmail(String to, String subject, String body, String pathToAttachment)
            throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        // helper.setText(body);
        helper.setFrom("noreply@springaouth.com");

        Context context = new Context();
        context.setVariable("name", to);
        context.setVariable("body", body);
        String emailContent = templateEngine.process("email-template", context);

        helper.setText(emailContent, true);

        if (pathToAttachment != null) {
            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);
        }

        javaMailSender.send(message);
    }

}
