package com.classreview.core.domain.entity;

import com.classreview.core.domain.enums.UrgencyLevel;

import com.classreview.core.domain.exceptions.InvalidFeedbackException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    void shouldCreateLowUrgencyFeedback() {

        Feedback feedback =
                new Feedback(
                        9,
                        "Muito bom"
                );

        assertEquals(
                UrgencyLevel.GOOD,
                feedback.getUrgency()
        );
    }

    @Test
    void shouldCreateMediumUrgencyFeedback() {

        Feedback feedback =
                new Feedback(
                        5,
                        "Professor razoável"
                );

        assertEquals(
                UrgencyLevel.MEDIUM,
                feedback.getUrgency()
        );
    }

    @Test
    void shouldCreateCriticalUrgencyFeedback() {

        Feedback feedback =
                new Feedback(
                        2,
                        "Professor faltou"
                );

        assertEquals(
                UrgencyLevel.CRITICAL,
                feedback.getUrgency()
        );
    }

    @Test
    void shouldThrowExceptionWhenRatingIsLessThanZero() {

        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback(
                        -1,
                        "Comentário"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenRatingIsGreaterThanTen() {

        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback(
                        11,
                        "Comentário"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenCommentIsNull() {

        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback(
                        5,
                        null
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenCommentIsBlank() {

        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback(
                        5,
                        "   "
                )
        );
    }
}