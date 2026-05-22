package com.classreview.infra.web.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;

import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;

import jakarta.ws.rs.ext.Provider;

import org.jboss.logging.MDC;

import java.io.IOException;
import java.util.UUID;

@Provider
public class CorrelationIdFilter
        implements ContainerRequestFilter,
        ContainerResponseFilter {

    @Override
    public void filter(
            ContainerRequestContext requestContext
    ) throws IOException {

        String correlationId =
                requestContext.getHeaderString(
                        CorrelationIdConstants.HEADER_NAME
                );

        if (correlationId == null
                || correlationId.isBlank()) {

            correlationId =
                    UUID.randomUUID().toString();
        }

        MDC.put(
                CorrelationIdConstants.MDC_KEY,
                correlationId
        );

        requestContext.setProperty(
                CorrelationIdConstants.MDC_KEY,
                correlationId
        );
    }

    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext
    ) throws IOException {

        Object correlationId =
                requestContext.getProperty(
                        CorrelationIdConstants.MDC_KEY
                );

        if (correlationId != null) {

            responseContext.getHeaders().add(
                    CorrelationIdConstants.HEADER_NAME,
                    correlationId.toString()
            );
        }

        MDC.clear();
    }
}