package com.classreview.infra.persistence.dynamo.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@ApplicationScoped
public class DynamoConfig {

    @Produces
    @ApplicationScoped
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(
            DynamoDbClient dynamoDbClient
    ) {

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}
