package com.classreview.core.application.dto.feedback;

public record FeedbackRequestDTO(
        int rating,
        String comment
) {
}
