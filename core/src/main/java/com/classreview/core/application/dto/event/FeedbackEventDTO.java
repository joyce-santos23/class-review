package com.classreview.core.application.dto.event;

import com.classreview.core.domain.entity.Feedback;

import java.time.Instant;

public record FeedbackEventDTO(
        String id,
        String description,
        String urgency,
        Instant createdAt
) {

    public static FeedbackEventDTO from(Feedback feedback) {
        return new FeedbackEventDTO(
                feedback.getId(),
                feedback.getComment(),
                feedback.getUrgency().name(),
                feedback.getCreatedAt()

        );
    }
}
