package com.classreview.infra.dto;

import java.time.Instant;

public record FeedbackEventDTO(
        String id,
        String description,
        String urgency,
        Instant createdAt,
        String correlationId
) {


}
