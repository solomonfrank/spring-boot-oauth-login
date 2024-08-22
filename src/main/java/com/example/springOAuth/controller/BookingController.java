package com.example.springOAuth.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springOAuth.entity.Booking;
import com.example.springOAuth.entity.User;
import com.example.springOAuth.model.BookingRequest;
import com.example.springOAuth.repository.BookingRepository;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.response.BookingResponse;
import com.example.springOAuth.response.PagedResponse;
import com.example.springOAuth.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
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
    public ResponseEntity<?> getBooking(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal User currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        int pageSize = Math.min(size > 0 ? size : 10, 15);

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), pageSize, Sort.by("createdAt").descending());

        Page<Booking> bookings = bookingRepository.findAllByUser(user, pageable);

        var mappedBooking = bookings.getContent().stream()
                .map(booking -> modelMapper.map(booking, BookingResponse.class)).toList();

        var response = PagedResponse.<BookingResponse>builder().data(mappedBooking)
                .totalElements(bookings.getTotalElements()).totalPages(bookings.getTotalPages())
                .first(bookings.isFirst()).last(bookings.isLast())
                .size(bookings.getSize())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    public ResponseEntity<?> bookEvent(@Valid @RequestBody BookingRequest entity)
            throws FileNotFoundException, MessagingException, IOException {
        var booking = bookingService.handleBooking(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

}
