package com.classreview.infra.web.payload.report;

public record FeedbackSummaryResponsePayload(
        Integer rating,
        String description,
        String urgency,
        String createdAt
) {
}