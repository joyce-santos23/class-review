package com.web.mapper.report;


import com.classreview.core.application.dto.report.FeedbackSummaryDTO;
import com.classreview.core.application.dto.report.WeeklyReportDTO;

import com.web.payload.report.FeedbackSummaryResponsePayload;
import com.web.payload.report.WeeklyReportResponsePayload;

public class ReportWebMapper {

    private ReportWebMapper() {
    }

    public static WeeklyReportResponsePayload toResponse(
            WeeklyReportDTO dto
    ) {

        return new WeeklyReportResponsePayload(
                dto.feedbacks()
                        .stream()
                        .map(ReportWebMapper::toSummaryResponse)
                        .toList(),

                dto.feedbacksPerDay(),

                dto.feedbacksByUrgency(),

                dto.averageRating()
        );
    }

    private static FeedbackSummaryResponsePayload toSummaryResponse(
            FeedbackSummaryDTO dto
    ) {

        return new FeedbackSummaryResponsePayload(
                dto.rating(),
                dto.description(),
                dto.urgency(),
                dto.createdAt().toString()
        );
    }
}
