package com.classreview.infra.web.payload.feedback;

public record FeedbackResponsePayload(
        String id,
        int rating,
        String comment,
        String urgency
) {
}
