package com.example.springOAuth.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.example.springOAuth.service.Email.IEmailProvider;
import com.example.springOAuth.service.Email.SmtpConfigModel;
import com.example.springOAuth.service.Email.SmtpEmailProvider;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        // mailSender.setPort(587);

        mailSender.setUsername("solomonrock13@gmail.com");
        mailSender.setPassword("ijwk qghc fcfk kcan");

        Properties props = mailSender.getJavaMailProperties();
        // props.put("mail.transport.protocol", "smtp");
        // props.put("mail.smtp.auth", "true");
        // props.put("mail.smtp.starttls.enable", true);
        // props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        // props.put("mail.debug", "true");

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        // props.put("mail.smtp.starttls.enable", false);
        props.put("mail.smtp.ssl.enable", true);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.debug", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");

        return mailSender;
    }

    @Bean
    public IEmailProvider getSmtpEmailProvider() {
        var config = SmtpConfigModel.builder()
                .auth(true)
                .password("ijwk qghc fcfk kcan")
                .username("solomonrock13@gmail.com")
                .host("smtp.gmail.com")
                .port(465)
                .debug("true")
                .enableSSl(true)
                .starttlsEnable(false)
                .trust("smtp.gmail.com")
                .protocol("smtp")
                .build();
        return new SmtpEmailProvider(config);
    }
}
