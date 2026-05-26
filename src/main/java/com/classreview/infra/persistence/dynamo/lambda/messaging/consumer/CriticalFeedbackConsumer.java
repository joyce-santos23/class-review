package com.classreview.infra.persistence.dynamo.gateway.messaging.consumer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.classreview.core.application.dto.event.FeedbackEventDTO;
import com.classreview.core.domain.exceptions.ErrorReadingMessageException;
import com.classreview.infra.web.filter.CorrelationIdConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;

@Slf4j
public class CriticalFeedbackConsumer
        implements RequestHandler<SQSEvent, Void> {

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Override
    public Void handleRequest(
            SQSEvent event,
            Context context
    ) {

        event.getRecords().forEach(message -> {

            try {

                FeedbackEventDTO feedbackEvent =
                        objectMapper.readValue(
                                message.getBody(),
                                FeedbackEventDTO.class
                        );

                MDC.put(
                        CorrelationIdConstants.MDC_KEY,
                        feedbackEvent.correlationId()
                );

                processCriticalFeedback(
                        feedbackEvent
                );

            } catch (Exception e) {

                log.error(
                        "Failed to process message",
                        e
                );

                throw new ErrorReadingMessageException( "Failed to process message", e);
            }
        });

        return null;
    }

    private void processCriticalFeedback(
            FeedbackEventDTO event
    ) {

        log.warn(
                """
                CRITICAL FEEDBACK RECEIVED

                Id: {}
                Description: {}
                Urgency: {}
                CreatedAt: {}
                """,
                event.id(),
                event.description(),
                event.urgency(),
                event.createdAt()
        );
    }
}
