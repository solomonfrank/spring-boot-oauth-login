package com.example.springOAuth.response;

import java.time.ZonedDateTime;

import com.example.springOAuth.entity.BookStatus;

import lombok.Data;

@Data
public class BookingResponse {
    private Long id;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String title;
    private String description;
    private String location;
    private BookStatus bookStatus;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Long attendeeId;
    private Long eventTypeId;
    private Long userId;

}
