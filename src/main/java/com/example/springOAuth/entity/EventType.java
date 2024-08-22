package com.example.springOAuth.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_event_type", uniqueConstraints = @UniqueConstraint(columnNames = { "slug",
}), indexes = {
        @Index(columnList = "slug", name = "slug_index"),

})
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "slug")
    private String slug;

    @Column(nullable = true, name = "location")
    private String location;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "currency", nullable = true)
    private String currency;

    @Column(nullable = true, name = "description")
    private String description;

    @Column(nullable = false, name = "duration")
    private Long duration;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_USER_EVENT_TYPE"))
    // @JsonBackReference
    private User user;

}
