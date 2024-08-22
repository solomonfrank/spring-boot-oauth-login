package com.example.springOAuth.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springOAuth.entity.Attendee;
import com.example.springOAuth.entity.Booking;
import com.example.springOAuth.entity.EventType;
import com.example.springOAuth.exception.DuplicateUserInfoException;
import com.example.springOAuth.model.BookingRequest;
import com.example.springOAuth.model.PaymentRequest;
import com.example.springOAuth.model.UserDto;
import com.example.springOAuth.repository.AttendeeRepository;
import com.example.springOAuth.repository.BookingRepository;
import com.example.springOAuth.repository.EventTypeRepository;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.response.BookingAnalysisResponse;
import com.example.springOAuth.response.BookingResponse;
import com.example.springOAuth.response.PaymentResponse;
import com.example.springOAuth.service.Apps.CalendarService;
import com.example.springOAuth.service.Email.EmailService;
import com.example.springOAuth.service.payment.PaymentService;
import com.google.api.services.calendar.model.Event;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

        private final EventTypeRepository eventTypeRepository;
        private final BookingRepository bookingRepository;
        private final UserRepository userRepository;

        private final EmailService emailService;

        private final AttendeeRepository attendeeRepository;

        private final PaymentService paymentService;

        @Autowired
        private ModelMapper modelMapper;

        @Autowired
        private CalendarService calendarService;

        @Transactional
        public BookingResponse handleBooking(BookingRequest entity)
                        throws FileNotFoundException, MessagingException, IOException {

                EventType eventType = eventTypeRepository.findById(entity.getEventTypeId())
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
                                // .payment(null)
                                .build();

                var savedBooking = bookingRepository.save(booking);

                log.info("Booking Saved");

                // var price = O.ofNullable(eventType.getPrice()).orElse(BigDecimal.ZERO);
                var price = Optional.ofNullable(eventType.getPrice()).orElse(BigDecimal.ZERO);

                boolean bookRequirePayment = price.compareTo(BigDecimal.ZERO) > 0;

                PaymentResponse paymentResponse = null;
                if (bookRequirePayment && savedBooking != null) {
                        log.info("starting initialize payment");

                        PaymentRequest paymentReq = PaymentRequest.builder()
                                        .bookingId(savedBooking.getId())
                                        .email(entity.getAttendee().getEmail())
                                        .amount(price.toString()).build();

                        try {
                                paymentResponse = paymentService.initializePayment(paymentReq);

                        } catch (Exception ex) {

                                log.debug("Unable to process transaction", ex);
                        }

                }

                BookingResponse bookingResponse = modelMapper.map(savedBooking, BookingResponse.class);
                bookingResponse.setPaymentResponse(paymentResponse);

                UserDto user = modelMapper.map(host, UserDto.class);
                bookingResponse.setUser(user);

                try {
                        Event createdEvent = calendarService.createEventHandler(bookingResponse);

                        eventType.setLocation(createdEvent.getHangoutLink());
                        eventTypeRepository.save(eventType);

                        bookingResponse.setMeetingUrl(createdEvent.getHangoutLink());

                        emailService.sendEventBookRemainder(bookingResponse);
                        return bookingResponse;

                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        throw new IOException(e.getMessage());
                } catch (GeneralSecurityException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        throw new IOException(e.getMessage());
                }

        }

        public BookingAnalysisResponse getAnalysisHandler(Long userId) {

                ZonedDateTime now = ZonedDateTime.now();
                long upcomingBooking = bookingRepository.getUpComingBookingCount(userId, now);
                long pastBooking = bookingRepository.getPastBookingCount(userId, now);

                return BookingAnalysisResponse.builder().totalPastCount(pastBooking).totalUpcomingCount(upcomingBooking)
                                .build();

        }

}
