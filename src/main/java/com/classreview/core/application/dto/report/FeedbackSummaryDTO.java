package com.classreview.core.application.dto.report;

import com.classreview.core.domain.entity.Feedback;

import java.time.Instant;

public record FeedbackSummaryDTO(
        String description,
        String urgency,
        Instant createdAt
) {

    public static FeedbackSummaryDTO from(Feedback feedback) {
        return new FeedbackSummaryDTO(
                feedback.getComment(),
                feedback.getUrgency().name(),
                feedback.getCreatedAt()
        );
    }
}