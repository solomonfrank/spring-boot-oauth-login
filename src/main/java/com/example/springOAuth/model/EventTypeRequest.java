package com.example.springOAuth.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventTypeRequest {
    @NotBlank(message = "Title is required.")
    private String title;

    private String description;

    @NotNull(message = "Duration is required")
    private Long duration;
}
