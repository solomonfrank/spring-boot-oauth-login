package com.example.springOAuth.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springOAuth.entity.Attendee;
import com.example.springOAuth.entity.Booking;
import com.example.springOAuth.entity.User;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    public List<Booking> findAllByUser(User user);

    boolean existsByAttendeeAndStartDateEquals(Attendee attendee, ZonedDateTime startDate);

    // @Query(value = "SELECT * FROM tbl_booking AS b LEFT JOIN tbl_attendees AS a
    // on a.id = b.attendee_id where a.email = :email and b.start_date = :startDate
    // LIMIT 1", nativeQuery = true)

    @Query("SELECT b from Booking b  JOIN b.attendee WHERE b.attendee.email = :email AND b.startDate = :startDate  ")
    Optional<Booking> findByAttendeeAndStartDate(@Param("startDate") ZonedDateTime startDate,
            @Param("email") String email);

}
