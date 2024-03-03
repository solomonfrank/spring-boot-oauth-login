package com.example.springOAuth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springOAuth.entity.Booking;
import com.example.springOAuth.entity.User;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    public List<Booking> findAllByUser(User user);

}
