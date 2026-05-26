package com.web.controller.report;

import com.classreview.core.domain.entity.Feedback;
import com.classreview.core.domain.enums.UrgencyLevel;
import com.persistence.dynamo.lambda.feedback.FeedbackRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class WeeklyReportControllerIT {

    @Inject
    FeedbackRepository feedbackRepository;

    @BeforeEach
    void setup() {

        Feedback critical = Feedback.restore(
                "1",
                2,
                "Muito ruim",
                Instant.now(),
                UrgencyLevel.CRITICAL
        );

        Feedback good = Feedback.restore(
                "2",
                8,
                "Muito bom",
                Instant.now(),
                UrgencyLevel.GOOD
        );

        feedbackRepository.save(critical);
        feedbackRepository.save(good);
    }

    @Test
    void shouldGenerateWeeklyReport() {

        given()
                .when()
                .get("/reports/weekly")
                .then()
                .statusCode(200)

                .body("feedbacks", notNullValue())

                .body(
                        "feedbacks.size()",
                        notNullValue()
                )

                .body(
                        "feedbacksByUrgency.CRITICAL",
                        notNullValue()
                )

                .body(
                        "feedbacksByUrgency.GOOD",
                        notNullValue()
                )

                .body(
                        "feedbacksPerDay",
                        notNullValue()
                )

                .body(
                        "averageRating",
                        notNullValue()
                );
    }
}