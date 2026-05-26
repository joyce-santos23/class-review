package com.classreview.core.domain.exceptions;

public class InvalidFeedbackException extends RuntimeException {
    public InvalidFeedbackException(String message) {
        super(message);
    }
}
