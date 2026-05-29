package com.persistence.dynamo.lambda.messaging.producer;

import com.classreview.core.application.dto.event.FeedbackEventDTO;
import com.classreview.core.application.gateway.notification.NotificationPublisher;
import com.classreview.core.domain.exceptions.InvalidFeedbackException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@ApplicationScoped
public class CriticalFeedbackProducer
        implements NotificationPublisher {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @ConfigProperty(name = "critical-feedback.queue.url")
    String queueUrl;

    public CriticalFeedbackProducer(
            SqsClient sqsClient
    ) {

        this.sqsClient = sqsClient;

        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Override
    public void publishCriticalFeedback(
            FeedbackEventDTO event
    ) {

        try {

            log.info(
                    "Publishing critical feedback event to SQS"
            );

            String messageBody =
                    objectMapper.writeValueAsString(event);

            SendMessageRequest request =
                    SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(messageBody)
                            .build();

            var response =
                    sqsClient.sendMessage(request);

            log.info(
                    "Critical feedback event published successfully. MessageId={}",
                    response.messageId()
            );

        } catch (Exception e) {

            log.error(
                    "Error publishing critical feedback event",
                    e
            );

            throw new InvalidFeedbackException(
                    "Erro ao enviar feedback crítico para SQS"
            );
        }
    }
}
