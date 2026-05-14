package com.classreview.core.domain.entity;

import com.classreview.core.domain.enums.UrgencyLevel;
import com.classreview.core.domain.exceptions.InvalidFeedbackException;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Feedback {

    private String id;
    private final int rating;
    private final String comment;
    private Instant createdAt;
    private UrgencyLevel urgency;

    // construtor para criação nova
    public Feedback(int rating, String comment) {
        validate(rating, comment);

        this.id = java.util.UUID.randomUUID().toString();
        this.rating = rating;
        this.comment = comment;
        this.createdAt = Instant.now();
        this.urgency = UrgencyLevel.fromRating(rating);
    }

    public static Feedback restore(String id, int rating, String comment, Instant createdAt, UrgencyLevel urgency) {
        Feedback feedback = new Feedback(rating, comment);
        feedback.id = id;
        feedback.createdAt = createdAt;
        feedback.urgency = urgency;
        return feedback;
    }

    private void validate(int rating, String comment) {
        if (rating < 0 || rating > 10) {
            throw new InvalidFeedbackException("Nota deve estar entre 0 e 10");
        }

        if (comment == null || comment.isBlank()) {
            throw new InvalidFeedbackException("Descrição é obrigatória");
        }
    }

    public boolean isCritical() {
        return urgency == UrgencyLevel.CRITICAL;
    }
}