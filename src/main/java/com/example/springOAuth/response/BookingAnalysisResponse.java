package com.example.springOAuth.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingAnalysisResponse {
    private long totalUpcomingCount;
    private long totalPastCount;
}
