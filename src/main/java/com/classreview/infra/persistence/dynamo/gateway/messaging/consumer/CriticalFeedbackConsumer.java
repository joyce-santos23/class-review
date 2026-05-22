package com.classreview.infra.persistence.dynamo.gateway.messaging.consumer;

import com.classreview.core.application.dto.event.FeedbackEventDTO;
import com.classreview.core.domain.exceptions.ErrorReadingMessageException;
import com.classreview.infra.web.filter.CorrelationIdConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import org.jboss.logging.MDC;

import java.util.List;

@Slf4j
@ApplicationScoped
public class CriticalFeedbackConsumer {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final String queueUrl;

    public CriticalFeedbackConsumer(SqsClient sqsClient,
                                    ObjectMapper objectMapper,
                                    @ConfigProperty(name = "critical-feedback.queue.url")
                                    String queueUrl) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.queueUrl = queueUrl;
    }

    @Scheduled(every = "10s")
    void consumeMessages() {

        ReceiveMessageRequest request =
                ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .maxNumberOfMessages(10)
                        .build();

        List<Message> messages =
                sqsClient.receiveMessage(request)
                        .messages();

        if (messages.isEmpty()) {
            return;
        }

        log.info("Received {} critical feedback messages", messages.size());

        for (Message message : messages) {

            try {

                FeedbackEventDTO event =
                        objectMapper.readValue(
                                message.body(),
                                FeedbackEventDTO.class
                        );

                MDC.put(
                        CorrelationIdConstants.MDC_KEY,
                        event.correlationId()
                );

                processCriticalFeedback(event);

                deleteMessage(message);

            } catch (Exception e) {
                throw new ErrorReadingMessageException(
                        "Failed to process critical feedback message",
                        e
                );
            }
        }
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

    private void deleteMessage(
            Message message
    ) {

        DeleteMessageRequest deleteRequest =
                DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(
                                message.receiptHandle()
                        )
                        .build();

        sqsClient.deleteMessage(deleteRequest);

        log.info("Message deleted from queue");
    }

}
