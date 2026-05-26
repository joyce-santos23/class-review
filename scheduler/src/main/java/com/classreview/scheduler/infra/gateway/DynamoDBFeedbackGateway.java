package com.classreview.scheduler.infra.gateway;

import com.classreview.core.application.gateway.feedback.FeedbackGateway;
import com.classreview.core.domain.entity.Feedback;
import com.classreview.core.domain.enums.UrgencyLevel;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamoDBFeedbackGateway implements FeedbackGateway {

    private final DynamoDbClient dynamoDb = DynamoDbClient.create();
    private final String TABLE_NAME = "Feedback";

    @Override
    public void save(Feedback feedback) {
        throw new UnsupportedOperationException("Scheduler only reads");
    }

    @Override
    public List<Feedback> findByPeriod(Instant start, Instant end) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("createdAt BETWEEN :start AND :end")
                .expressionAttributeValues(Map.of(
                        ":start", AttributeValue.builder().s(start.toString()).build(),
                        ":end", AttributeValue.builder().s(end.toString()).build()
                ))
                .build();

        ScanResponse response = dynamoDb.scan(scanRequest);

        List<Feedback> feedbacks = new ArrayList<>();

        for (Map<String, AttributeValue> item : response.items()) {
            Feedback feedback = mapToFeedback(item);
            feedbacks.add(feedback);
        }

        return feedbacks;
    }

    private Feedback mapToFeedback(Map<String, AttributeValue> item) {
        // Usando o factory method restore()
        String id = item.get("id").s();
        int rating = Integer.parseInt(item.get("rating").n());
        String comment = item.get("comment").s();
        Instant createdAt = Instant.parse(item.get("createdAt").s());
        UrgencyLevel urgency = UrgencyLevel.valueOf(item.get("urgency").s());

        return Feedback.restore(id, rating, comment, createdAt, urgency);
    }
}