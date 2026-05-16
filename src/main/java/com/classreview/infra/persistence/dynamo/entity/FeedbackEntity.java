package com.classreview.infra.persistence.dynamo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Getter
@Setter
@NoArgsConstructor
@DynamoDbBean
public class FeedbackEntity {
    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private String id;
    private int rating;
    private String comment;
    private String urgency;
    private String createdAt;

}
