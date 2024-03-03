package com.example.springOAuth.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springOAuth.entity.User;
import com.example.springOAuth.model.BookingRequest;
import com.example.springOAuth.repository.BookingRepository;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.response.BookingResponse;
import com.example.springOAuth.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("")
    public ResponseEntity<?> getBooking(@AuthenticationPrincipal User currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        var bookings = bookingRepository.findAllByUser(user);

        var mappedBooking = bookings.stream().map(booking -> modelMapper.map(booking, BookingResponse.class));

        return ResponseEntity.status(HttpStatus.OK).body(mappedBooking);
    }

    @PostMapping("")
    public ResponseEntity<?> bookEvent(@Valid @RequestBody BookingRequest entity) {
        var booking = bookingService.handleBooking(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

}
