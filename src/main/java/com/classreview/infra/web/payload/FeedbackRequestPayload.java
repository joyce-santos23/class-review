package com.classreview.infra.web.payload;

public record FeedbackRequestPayload(
        int rating,
        String comment
) {
}
