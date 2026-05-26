package com.web.filter;

public final class CorrelationIdConstants {

    private CorrelationIdConstants() {
    }

    public static final String HEADER_NAME =
            "X-Correlation-Id";

    public static final String MDC_KEY =
            "correlationId";
}