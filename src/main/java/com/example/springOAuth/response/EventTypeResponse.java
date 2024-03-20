package com.example.springOAuth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EventTypeResponse {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "Demo")
    private String title;
    @Schema(example = "description")
    private String description;
    @Schema(example = "15")
    private Long duration;
    private Long userId;
    @Schema(example = "description-mvp")
    private String slug;
    @Schema(example = "John doe")
    private String owner;
}
