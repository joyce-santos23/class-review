package com.classreview.core.application.usecase.feedback;

import com.classreview.core.application.dto.event.FeedbackEventDTO;
import com.classreview.core.application.dto.feedback.FeedbackRequestDTO;
import com.classreview.core.application.dto.feedback.FeedbackResponseDTO;
import com.classreview.core.application.gateway.feedback.FeedbackGateway;
import com.classreview.core.application.gateway.notification.NotificationPublisher;
import com.classreview.core.domain.entity.Feedback;

import com.classreview.infra.web.filter.CorrelationIdConstants;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;

@Slf4j
@ApplicationScoped
public class CreateFeedbackUseCase {


    private final FeedbackGateway feedbackGateway;
    private final NotificationPublisher publisher;

    public CreateFeedbackUseCase(
            FeedbackGateway feedbackGateway,
            NotificationPublisher publisher
    ) {

        this.feedbackGateway = feedbackGateway;
        this.publisher = publisher;
    }

    public FeedbackResponseDTO execute(
            FeedbackRequestDTO request
    ) {

        String correlationId =
                MDC.get(
                        CorrelationIdConstants.MDC_KEY
                ).toString();
        log.info(
                "Creating feedback with rating {}",
                request.rating()
        );


        Feedback feedback =
                new Feedback(
                        request.rating(),
                        request.comment()
                );

        feedbackGateway.save(feedback);

        if (feedback.isCritical()) {

            try {

                publisher.publishCriticalFeedback(
                        FeedbackEventDTO.from(feedback, correlationId)
                );

                log.info("Critical feedback event published");

            } catch (Exception e) {

                log.error(
                        "Error publishing critical feedback event",
                        e
                );
            }
        }

        return FeedbackResponseDTO.from(feedback);
    }
}