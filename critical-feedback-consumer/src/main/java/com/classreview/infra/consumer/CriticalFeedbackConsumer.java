package com.classreview.infra.consumer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.classreview.core.domain.exceptions.ErrorReadingMessageException;
import com.classreview.infra.dto.FeedbackEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Slf4j
public class CriticalFeedbackConsumer
        implements RequestHandler<SQSEvent, Void> {

    private final ObjectMapper objectMapper =
            new ObjectMapper()
                    .findAndRegisterModules();

    private final SnsClient snsClient = SnsClient.create();

    private final String topicArn = System.getenv("CRITICAL_FEEDBACK_SNS_TOPIC_ARN");

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

                processCriticalFeedback(feedbackEvent);

            } catch (Exception e) {
                log.error("Failed to process message", e);
                throw new ErrorReadingMessageException("Failed to process message", e);
            }
        });

        return null;
    }

    private void processCriticalFeedback(
            FeedbackEventDTO event
    ) {

        String notificationMessage =
                """
                CRITICAL FEEDBACK RECEIVED
                
                Id: %s
                Description: %s
                Urgency: %s
                CreatedAt: %s
                """
                        .formatted(
                                event.id(),
                                event.description(),
                                event.urgency(),
                                event.createdAt()
                        );

        log.info(notificationMessage);

        PublishRequest request =
                PublishRequest.builder()
                        .topicArn(topicArn)
                        .subject("Critical Feedback Alert")
                        .message(notificationMessage)
                        .build();

        snsClient.publish(request);

        log.info("Critical feedback notification sent successfully");
    }
}