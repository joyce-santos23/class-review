package com.classreview.infra.web.payload;

public record FeedbackResponsePayload(
        String id,
        int rating,
        String comment,
        String urgency
) {
}
