package com.example.springOAuth.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springOAuth.entity.User;
import com.example.springOAuth.model.UserProfileRequest;
import com.example.springOAuth.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PutMapping(value = "user/profile", consumes = { "multipart/form-data", "application/json" })

    public ResponseEntity<?> updateProfile(@RequestBody UserProfileRequest entity,

            @AuthenticationPrincipal User currentUser) throws IOException {

        var response = userService.updateProfile(currentUser, entity);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
