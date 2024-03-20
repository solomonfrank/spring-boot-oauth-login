package com.example.springOAuth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springOAuth.entity.Attendee;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

    public Optional<Attendee> findFirstByEmail(String email);

}
