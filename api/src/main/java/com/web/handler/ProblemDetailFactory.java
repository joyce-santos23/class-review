package com.web.handler;


import jakarta.ws.rs.core.Response;

import java.net.URI;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import java.util.Map;

public class ProblemDetailFactory {

    private ProblemDetailFactory() {
    }

    public static ProblemDetailResponse create(
            Response.Status status,
            String type,
            String title,
            String detail,
            Map<String, Object> properties
    ) {

        ProblemDetailResponse response =
                new ProblemDetailResponse(
                        URI.create(type),
                        title,
                        status.getStatusCode(),
                        detail,
                        OffsetDateTime.now(ZoneOffset.UTC),
                        properties
                );

        return response;
    }
}
