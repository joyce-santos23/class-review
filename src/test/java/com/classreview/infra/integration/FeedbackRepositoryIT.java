package com.classreview.infra.integration;

import com.classreview.core.domain.entity.Feedback;

import com.classreview.infra.persistence.dynamo.gateway.feedback.FeedbackRepository;

import io.quarkus.test.junit.QuarkusTest;

import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestProfile(IntegrationTestProfile.class)
class FeedbackRepositoryIT {

    @Inject
    FeedbackRepository repository;



    @Test
    void shouldSaveAndFindFeedbacks() {

        Feedback feedback =
                new Feedback(
                        2,
                        "Professor faltou"
                );

        repository.save(feedback);

        Instant start =
                Instant.now()
                        .minus(1, ChronoUnit.DAYS);

        Instant end =
                Instant.now()
                        .plus(1, ChronoUnit.DAYS);

        List<Feedback> feedbacks =
                repository.findByPeriod(
                        start,
                        end
                );

        assertFalse(
                feedbacks.isEmpty()
        );

        assertTrue(
                feedbacks.stream()
                        .anyMatch(f ->
                                f.getId()
                                        .equals(
                                                feedback.getId()
                                        )
                        )
        );
    }
}