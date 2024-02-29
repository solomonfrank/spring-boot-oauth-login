package com.example.springOAuth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("")
    public String getMethodName() {

        var name = "hello world";
        return name;
    }

}
