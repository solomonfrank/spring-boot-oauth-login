package com.example.springOAuth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springOAuth.entity.EventType;
import com.example.springOAuth.entity.User;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {

    List<EventType> findByUser(User user);

    Optional<EventType> findBySlug(String slug);

    @Query("Select e from EventType e JOIN e.user u WHERE e.user.email = :userId AND e.slug = :slug")
    Optional<EventType> getEventTypeEmailAddressAndSlug(@Param("userId") String emailAddress,
            @Param("slug") String slug);

}
