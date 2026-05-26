package com.classreview.core.application.dto.feedback;

import com.classreview.core.domain.entity.Feedback;

public record FeedbackResponseDTO(
        String id,
        int rating,
        String comment,
        String urgency
) {
    public static FeedbackResponseDTO from(Feedback feedback) {
        return new FeedbackResponseDTO(
                feedback.getId(),
                feedback.getRating(),
                feedback.getComment(),
                feedback.getUrgency().name()
        );
    }
}
