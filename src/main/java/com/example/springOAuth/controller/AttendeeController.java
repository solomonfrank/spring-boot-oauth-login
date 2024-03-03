package com.example.springOAuth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springOAuth.entity.User;
import com.example.springOAuth.model.AttendeeRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/attendee")
public class AttendeeController {

    @PostMapping("")
    public ResponseEntity<?> createAttendee(@Valid @RequestBody AttendeeRequest entity,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body("Atteendee created");
    }

    @GetMapping("path")
    public String getAttendee(@RequestParam String param) {
        return new String();
    }

}
