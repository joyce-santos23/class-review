package com.classreview.infra.web.controller.feedback;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class CreateFeedbackControllerIT {

    @Test
    void shouldCreateFeedback() {

        given()
                .contentType("application/json")
                .body("""
                        {
                          "rating": 2,
                          "comment": "Atendimento muito ruim"
                        }
                        """)
                .when()
                .post("/feedbacks")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("rating", equalTo(2))
                .body("comment", equalTo("Atendimento muito ruim"))
                .body("urgency", equalTo("CRITICAL"));
    }

    @Test
    void shouldReturnBadRequestWhenRatingIsInvalid() {

        given()
                .contentType("application/json")
                .body("""
                        {
                          "rating": 99,
                          "comment": "Teste"
                        }
                        """)
                .when()
                .post("/feedbacks")
                .then()
                .statusCode(400)
                .body("title", equalTo("Constraint Violation"))
                .body("status", equalTo(400))
                .body("violations[0].field",
                        equalTo("createFeedback.request.rating"))
                .body("violations[0].message",
                        equalTo("must be less than or equal to 10"));
    }

    @Test
    void shouldReturnBadRequestWhenCommentIsBlank() {

        given()
                .contentType("application/json")
                .body("""
                        {
                          "rating": 5,
                          "comment": ""
                        }
                        """)
                .when()
                .post("/feedbacks")
                .then()
                .statusCode(400)
                .body("title", equalTo("Constraint Violation"))
                .body("status", equalTo(400))
                .body("violations[0].field",
                        equalTo("createFeedback.request.comment"))
                .body("violations[0].message",
                        equalTo("must not be blank"));
    }

    @Test
    void shouldReturnCorrelationIdHeader() {

        given()
                .contentType("application/json")
                .header("X-Correlation-Id", "test-correlation-id")
                .body("""
                        {
                          "rating": 4,
                          "comment": "Problema resolvido"
                        }
                        """)
                .when()
                .post("/feedbacks")
                .then()
                .statusCode(201)
                .header(
                        "X-Correlation-Id",
                        equalTo("test-correlation-id")
                );
    }
}