package com.classreview.infra.persistence.dynamo.gateway.messaging;

import com.classreview.core.application.dto.event.FeedbackEventDTO;
import com.classreview.core.application.gateway.notification.NotificationPublisher;
import com.classreview.core.domain.exceptions.InvalidFeedbackException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@ApplicationScoped
public class SqsNotificationPublisher implements NotificationPublisher {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final String queueUrl;

    public SqsNotificationPublisher(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
        this.objectMapper = new ObjectMapper();
        this.queueUrl = System.getenv("SQS_CRITICAL_FEEDBACK_URL"); // URL da fila no AWS
    }

    @Override
    public void publishCriticalFeedback(FeedbackEventDTO event) {
        try {
            String messageBody = objectMapper.writeValueAsString(event);
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build();
            sqsClient.sendMessage(request);
        } catch (Exception e) {
            throw new InvalidFeedbackException("Erro ao enviar feedback crítico para SQS " + e);
        }
    }
}
