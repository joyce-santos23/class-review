package com.classreview.infra.web.handler;

import java.net.URI;

import java.time.OffsetDateTime;

import java.util.Map;

public record ProblemDetailResponse(
        URI type,
        String title,
        int status,
        String detail,
        OffsetDateTime timestamp,
        Map<String, Object> properties
) {
}
