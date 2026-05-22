package com.classreview.infra.web.payload.report;


import com.classreview.infra.web.payload.feedback.FeedbackResponsePayload;

import java.util.List;
import java.util.Map;

public record WeeklyReportResponsePayload(
        List<FeedbackSummaryResponsePayload> feedbacks,
        Map<String, Long> feedbacksPerDay,
        Map<String, Long> feedbacksByUrgency,
        double averageRating
) {
}
