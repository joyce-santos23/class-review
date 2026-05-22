package com.classreview.core.application.usecase.feedback;

import com.classreview.core.application.dto.feedback.FeedbackRequestDTO;
import com.classreview.core.application.gateway.feedback.FeedbackGateway;
import com.classreview.core.application.gateway.notification.NotificationPublisher;
import com.classreview.infra.web.filter.CorrelationIdConstants;
import org.jboss.logging.MDC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CreateFeedbackUseCaseTest {

    private FeedbackGateway feedbackGateway;

    private NotificationPublisher notificationPublisher;

    private CreateFeedbackUseCase useCase;

    @BeforeEach
    void setUp() {

        MDC.put(
                CorrelationIdConstants.MDC_KEY,
                "test-correlation-id"
        );

        feedbackGateway =
                mock(FeedbackGateway.class);

        notificationPublisher =
                mock(NotificationPublisher.class);

        useCase =
                new CreateFeedbackUseCase(
                        feedbackGateway,
                        notificationPublisher
                );
    }

    @Test
    void shouldSaveFeedback() {

        FeedbackRequestDTO request =
                new FeedbackRequestDTO(
                        8,
                        "Muito bom"
                );

        useCase.execute(request);

        verify(feedbackGateway)
                .save(any());
    }

    @Test
    void shouldPublishCriticalFeedbackEvent() {

        FeedbackRequestDTO request =
                new FeedbackRequestDTO(
                        2,
                        "Professor faltou"
                );

        useCase.execute(request);

        verify(notificationPublisher)
                .publishCriticalFeedback(any());
    }

    @Test
    void shouldNotPublishEventForNonCriticalFeedback() {

        FeedbackRequestDTO request =
                new FeedbackRequestDTO(
                        9,
                        "Muito bom"
                );

        useCase.execute(request);

        verify(
                notificationPublisher,
                never()
        ).publishCriticalFeedback(any());
    }

    @AfterEach
    void tearDown() {

        MDC.clear();
    }
}