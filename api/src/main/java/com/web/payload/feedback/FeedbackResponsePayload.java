package com.web.payload.feedback;

public record FeedbackResponsePayload(
        String id,
        int rating,
        String comment,
        String urgency
) {
}
