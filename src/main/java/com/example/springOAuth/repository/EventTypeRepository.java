package com.example.springOAuth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springOAuth.entity.EventType;
import com.example.springOAuth.entity.User;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {

    List<EventType> findByUser(User user);

}
