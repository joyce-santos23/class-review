package com.classreview.core.application.dto.report;

import java.util.List;
import java.util.Map;

public record WeeklyReportDTO(
        List<FeedbackSummaryDTO> feedbacks,
        Map<String, Long> feedbacksPerDay,
        Map<String, Long> feedbacksByUrgency,
        double averageRating
) {
}
