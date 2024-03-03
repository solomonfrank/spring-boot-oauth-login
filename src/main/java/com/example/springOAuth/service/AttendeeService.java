package com.example.springOAuth.service;

import org.springframework.stereotype.Service;

import com.example.springOAuth.entity.Attendee;
import com.example.springOAuth.entity.User;
import com.example.springOAuth.model.AttendeeRequest;
import com.example.springOAuth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final UserRepository uswRepository;

    public void createAttendee(AttendeeRequest request, User currentUser) {

        Attendee.builder().email(currentUser.getEmail()).name(request.getName()).timeZone(request.getTimeZone())
                .build();

    }

}
