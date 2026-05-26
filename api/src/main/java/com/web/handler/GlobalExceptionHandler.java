package com.web.handler;

import com.classreview.core.domain.exceptions.ErrorReadingMessageException;
import com.classreview.core.domain.exceptions.InvalidFeedbackException;

import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class GlobalExceptionHandler
        implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(
            Exception exception
    ) {

        if (exception instanceof InvalidFeedbackException ex) {

            ProblemDetailResponse response =
                    ProblemDetailFactory.create(
                            Response.Status.BAD_REQUEST,
                            "/errors/invalid-feedback",
                            "Invalid feedback",
                            ex.getMessage(),
                            null
                    );

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }

        if (exception instanceof ErrorReadingMessageException ex) {

            ProblemDetailResponse response =
                    ProblemDetailFactory.create(
                            Response.Status.INTERNAL_SERVER_ERROR,
                            "/errors/message-processing-error",
                            "Message processing error",
                            ex.getMessage(),
                            null
                    );

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }

        log.error(
                "Unexpected internal server error",
                exception
        );

        ProblemDetailResponse response =
                ProblemDetailFactory.create(
                        Response.Status.INTERNAL_SERVER_ERROR,
                        "/errors/internal-server-error",
                        "Internal server error",
                        "Unexpected internal server error",
                        null
                );

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
    }
}