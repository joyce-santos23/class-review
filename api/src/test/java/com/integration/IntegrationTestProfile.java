package com.integration;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class IntegrationTestProfile
        implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {

        return Map.of(
                "quarkus.http.port", "0",
                "quarkus.dynamodb.endpoint-override", "http://localhost:4566",
                "quarkus.sqs.endpoint-override", "http://localhost:4566",
                "critical-feedback.queue.url",
                "http://localhost:4566/000000000000/critical-feedback"
        );
    }
}