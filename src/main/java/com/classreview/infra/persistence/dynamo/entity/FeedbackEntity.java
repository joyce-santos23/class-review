package com.classreview.infra.persistence.dynamo.entity;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Getter
@Setter
@DynamoDbBean
public class FeedbackEntity {

    private String id;
    private int rating;
    private String comment;
    private String urgency;
    private String createdAt;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
