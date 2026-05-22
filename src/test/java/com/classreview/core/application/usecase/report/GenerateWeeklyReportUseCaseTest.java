package com.classreview.core.application.usecase.report;

import com.classreview.core.application.dto.report.WeeklyReportDTO;
import com.classreview.core.application.gateway.feedback.FeedbackGateway;
import com.classreview.core.domain.entity.Feedback;

import com.classreview.core.domain.enums.UrgencyLevel;
import com.classreview.infra.web.filter.CorrelationIdConstants;

import org.jboss.logging.MDC;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GenerateWeeklyReportUseCaseTest {

    private FeedbackGateway feedbackGateway;

    private GenerateWeeklyReportUseCase useCase;

    @BeforeEach
    void setUp() {

        MDC.put(
                CorrelationIdConstants.MDC_KEY,
                "test-correlation-id"
        );

        feedbackGateway =
                mock(FeedbackGateway.class);

        useCase =
                new GenerateWeeklyReportUseCase(
                        feedbackGateway
                );
    }

    @AfterEach
    void tearDown() {

        MDC.clear();
    }

    @Test
    void shouldGenerateWeeklyReport() {

        Feedback feedback1 =
                Feedback.restore(
                        "1",
                        2,
                        "Professor faltou",
                        Instant.now(),
                        com.classreview.core.domain.enums.UrgencyLevel.CRITICAL
                );

        Feedback feedback2 =
                Feedback.restore(
                        "2",
                        8,
                        "Muito bom",
                        Instant.now(),
                        UrgencyLevel.GOOD
                );

        when(
                feedbackGateway.findByPeriod(
                        any(),
                        any()
                )
        ).thenReturn(
                List.of(
                        feedback1,
                        feedback2
                )
        );

        WeeklyReportDTO report =
                useCase.execute();

        assertNotNull(report);

        assertEquals(
                2,
                report.feedbacks().size()
        );

        assertEquals(
                5.0,
                report.averageRating()
        );

        assertEquals(
                1L,
                report.feedbacksByUrgency()
                        .get(UrgencyLevel.CRITICAL.name())
        );

        assertEquals(
                1L,
                report.feedbacksByUrgency()
                        .get(UrgencyLevel.GOOD.name())
        );

        verify(feedbackGateway)
                .findByPeriod(
                        any(),
                        any()
                );
    }
}