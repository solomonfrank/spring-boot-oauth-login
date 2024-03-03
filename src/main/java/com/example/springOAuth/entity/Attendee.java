package com.example.springOAuth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_attendees")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(nullable = false, name = "email")
    private String email;

    @NotBlank
    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = true, name = "time_zone")
    private String timeZone;

    // @OneToMany(mappedBy = "attendee", fetch = FetchType.LAZY)
    // @JsonManagedReference
    // private List<Booking> booking;
}
