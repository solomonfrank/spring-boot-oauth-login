package com.example.springOAuth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springOAuth.entity.Attendee;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

}
