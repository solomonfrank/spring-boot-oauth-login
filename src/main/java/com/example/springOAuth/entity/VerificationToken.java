package com.example.springOAuth.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Table(name = "tbl_verification_token")
@Builder
@AllArgsConstructor
public class VerificationToken {

    public static final int EXPIRATION_TIME_IN_SECOND = 24 * 3600 * 1000; // +1day

    public VerificationToken() {
        this.createdAt = ZonedDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;

    private String token;

    private ZonedDateTime expires;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

}
