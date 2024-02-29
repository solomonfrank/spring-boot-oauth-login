package com.example.springOAuth.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private String applicationUrl;
    private String email;

    public RegistrationCompleteEvent(String email, String applicationUrl) {
        super(email);
        this.applicationUrl = applicationUrl;
        this.email = email;
    }

}
