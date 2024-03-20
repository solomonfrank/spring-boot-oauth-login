package com.example.springOAuth.service;

import java.time.ZonedDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springOAuth.entity.Attendee;
import com.example.springOAuth.entity.Booking;
import com.example.springOAuth.exception.DuplicateUserInfoException;
import com.example.springOAuth.model.BookingRequest;
import com.example.springOAuth.repository.AttendeeRepository;
import com.example.springOAuth.repository.BookingRepository;
import com.example.springOAuth.repository.EventTypeRepository;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.response.BookingResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

        private final EventTypeRepository eventTypeRepository;
        private final BookingRepository bookingRepository;
        private final UserRepository userRepository;

        private final AttendeeRepository attendeeRepository;

        @Autowired
        private ModelMapper modelMapper;

        @Transactional
        public BookingResponse handleBooking(BookingRequest entity) {

                var eventType = eventTypeRepository.findById(entity.getEventTypeId())
                                .orElseThrow(() -> new UsernameNotFoundException("Event does not exist"));

                var userId = eventType.getUser().getId();

                var host = userRepository.findById(userId)
                                .orElseThrow(() -> new UsernameNotFoundException("Host does not exist"));
                var attendee = Attendee.builder().email(entity.getAttendee().getEmail())
                                .phoneNumber(entity.getAttendee().getPhoneNumber())
                                .name(entity.getAttendee().getName())
                                .timeZone(entity.getAttendee().getTimeZone()).build();

                var attendeeExist = attendeeRepository.findFirstByEmail(entity.getAttendee().getEmail()).orElse(null);

                if (attendeeExist != null) {
                        var bookingExist = bookingRepository.findByAttendeeAndStartDate(
                                        entity.getStartDate(), attendeeExist.getEmail());

                        if (bookingExist.isPresent()) {
                                throw new DuplicateUserInfoException("Already booked this event");
                        }

                }

                var booking = Booking.builder().title(entity.getTitle()).description(entity.getDescription())
                                .location(entity.getLocation()).attendee(attendee)
                                .startDate(entity.getStartDate())
                                .endDate(entity.getEndDate())
                                .updatedAt(ZonedDateTime.now())
                                .eventType(eventType)
                                .user(host)
                                .build();

                var savedBooking = bookingRepository.save(booking);

                return modelMapper.map(savedBooking, BookingResponse.class);

        }

}
