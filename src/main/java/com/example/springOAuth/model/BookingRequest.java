package com.example.springOAuth.model;

import java.time.ZonedDateTime;

import com.example.springOAuth.entity.BookStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {

    @NotNull(message = "Start date is required.")
    private ZonedDateTime startDate;

    @NotNull(message = "End date is required.")
    private ZonedDateTime endDate;

    @NotBlank(message = "Title is required.")
    private String title;

    private String description;

    private String location;

    private BookStatus bookStatus;

    @NotNull(message = "Attendee is required.")
    private AttendeeRequest attendee;

    @NotNull(message = "Event id is required.")
    private Long eventTypeId;
}
