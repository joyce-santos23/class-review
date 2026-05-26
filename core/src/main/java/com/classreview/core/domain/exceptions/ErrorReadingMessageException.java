package com.classreview.core.domain.exceptions;

public class ErrorReadingMessageException extends RuntimeException {
    public ErrorReadingMessageException(String message, Exception e) {
        super(message, e);
    }
}
