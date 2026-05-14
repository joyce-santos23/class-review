package com.classreview.core.application.usecase.report;

import com.classreview.core.application.dto.report.FeedbackSummaryDTO;
import com.classreview.core.application.dto.report.WeeklyReportDTO;
import com.classreview.core.application.gateway.feedback.FeedbackGateway;
import com.classreview.core.domain.entity.Feedback;
import com.classreview.core.domain.enums.UrgencyLevel;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class GenerateWeeklyReportUseCase {

    private final FeedbackGateway feedbackGateway;

    public GenerateWeeklyReportUseCase(FeedbackGateway feedbackGateway) {
        this.feedbackGateway = feedbackGateway;
    }

    public WeeklyReportDTO execute() {

        // intervalo correto (últimos 7 dias completos)
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        LocalDate startDate = today.minusDays(6);

        Instant start = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant end = today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        List<Feedback> feedbacks = feedbackGateway.findByPeriod(start, end);

        // lista resumida
        List<FeedbackSummaryDTO> summaries = feedbacks.stream()
                .map(FeedbackSummaryDTO::from)
                .toList();

        // quantidade por dia (com dias zerados)
        Map<String, Long> feedbacksPerDay = new LinkedHashMap<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            feedbacksPerDay.put(date.toString(), 0L);
        }

        feedbacks.forEach(f -> {
            String date = LocalDate.ofInstant(f.getCreatedAt(), ZoneOffset.UTC).toString();
            feedbacksPerDay.merge(date, 1L, Long::sum);
        });

        // quantidade por urgência (com todos os níveis)
        Map<String, Long> feedbacksByUrgency = new LinkedHashMap<>();

        for (UrgencyLevel level : UrgencyLevel.values()) {
            feedbacksByUrgency.put(level.name(), 0L);
        }

        feedbacks.forEach(f ->
                feedbacksByUrgency.merge(
                        f.getUrgency().name(),
                        1L,
                        Long::sum
                )
        );

        // média das avaliações
        double averageRating = feedbacks.stream()
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
}