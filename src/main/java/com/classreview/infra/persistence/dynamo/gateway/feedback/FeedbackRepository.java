package com.classreview.infra.persistence.dynamo.gateway.feedback;

import com.classreview.core.application.gateway.feedback.FeedbackGateway;
import com.classreview.core.domain.entity.Feedback;
import com.classreview.infra.persistence.dynamo.entity.FeedbackEntity;
import com.classreview.infra.persistence.dynamo.mapper.FeedbackMapper;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FeedbackRepository implements FeedbackGateway {

    private final DynamoDbTable<FeedbackEntity> table;

    public FeedbackRepository(DynamoDbEnhancedClient client) {
        this.table = client.table("Feedback", TableSchema.fromBean(FeedbackEntity.class));
    }
    @Override
    public void save(Feedback feedback) {
        table.putItem(FeedbackMapper.toEntity(feedback));
    }

    @Override
    public List<Feedback> findByPeriod(Instant start, Instant end) {
        return table.scan()
                .items()
                .stream()
                .map(FeedbackMapper::toDomain)
                .filter(f -> !f.getCreatedAt().isBefore(start) && !f.getCreatedAt().isAfter(end))
                .collect(Collectors.toList());
    }
}
