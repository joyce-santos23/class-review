package com.classreview.core.application.usecase.report;

import com.classreview.core.application.dto.report.FeedbackSummaryDTO;
import com.classreview.core.application.dto.report.WeeklyReportDTO;
import com.classreview.core.application.gateway.feedback.FeedbackGateway;
import com.classreview.core.domain.entity.Feedback;
import com.classreview.core.domain.enums.UrgencyLevel;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class GenerateWeeklyReportUseCase {

    private static final int REPORT_DAYS = 7;

    private final FeedbackGateway feedbackGateway;

    public GenerateWeeklyReportUseCase(
            FeedbackGateway feedbackGateway
    ) {

        this.feedbackGateway = feedbackGateway;
    }

    public WeeklyReportDTO execute() {

        log.info("Generating weekly feedback report");

        LocalDate today =
                LocalDate.now(ZoneOffset.UTC);

        LocalDate startDate =
                today.minusDays(REPORT_DAYS - 1);

        Instant start =
                startDate
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC);

        Instant end =
                today
                        .plusDays(1)
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC);

        List<Feedback> feedbacks =
                feedbackGateway.findByPeriod(
                        start,
                        end
                );

        log.info(
                "Found {} feedbacks for weekly report",
                feedbacks.size()
        );

        List<FeedbackSummaryDTO> summaries =
                feedbacks.stream()
                        .map(FeedbackSummaryDTO::from)
                        .toList();

        Map<String, Long> feedbacksPerDay =
                buildFeedbacksPerDay(
                        feedbacks,
                        startDate
                );

        Map<String, Long> feedbacksByUrgency =
                buildFeedbacksByUrgency(
                        feedbacks
                );

        double averageRating =
                feedbacks.stream()
                        .mapToInt(Feedback::getRating)
                        .average()
                        .orElse(0.0);

        return new WeeklyReportDTO(
                summaries,
                feedbacksPerDay,
                feedbacksByUrgency,
                averageRating
        );
    }

    private Map<String, Long> buildFeedbacksPerDay(
            List<Feedback> feedbacks,
            LocalDate startDate
    ) {

        Map<String, Long> feedbacksPerDay =
                new LinkedHashMap<>();

        for (int i = 0; i < REPORT_DAYS; i++) {

            LocalDate date =
                    startDate.plusDays(i);

            feedbacksPerDay.put(
                    date.toString(),
                    0L
            );
        }

        feedbacks.forEach(f -> {

            String date =
                    LocalDate.ofInstant(
                                    f.getCreatedAt(),
                                    ZoneOffset.UTC
                            )
                            .toString();

            feedbacksPerDay.merge(
                    date,
                    1L,
                    Long::sum
            );
        });

        return feedbacksPerDay;
    }

    private Map<String, Long> buildFeedbacksByUrgency(
            List<Feedback> feedbacks
    ) {

        Map<String, Long> feedbacksByUrgency =
                new LinkedHashMap<>();

        for (UrgencyLevel level :
                UrgencyLevel.values()) {

            feedbacksByUrgency.put(
                    level.name(),
                    0L
            );
        }

        feedbacks.forEach(f ->
                feedbacksByUrgency.merge(
                        f.getUrgency().name(),
                        1L,
                        Long::sum
                )
        );

        return feedbacksByUrgency;
    }
}