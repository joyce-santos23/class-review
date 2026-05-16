package com.classreview.infra.web.controller;

import com.classreview.core.application.dto.feedback.FeedbackResponseDTO;
import com.classreview.core.application.usecase.feedback.CreateFeedbackUseCase;
import com.classreview.infra.web.mapper.FeedbackWebMapper;
import com.classreview.infra.web.payload.FeedbackRequestPayload;
import com.classreview.infra.web.payload.FeedbackResponsePayload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/feedbacks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FeedbackController {

    private final CreateFeedbackUseCase createFeedbackUseCase;

    public FeedbackController(CreateFeedbackUseCase createFeedbackUseCase) {
        this.createFeedbackUseCase = createFeedbackUseCase;
    }

    @POST
    public FeedbackResponsePayload createFeedback(FeedbackRequestPayload request) {

        FeedbackResponseDTO response = createFeedbackUseCase.execute(FeedbackWebMapper.toDTO(request));
        return FeedbackWebMapper.toResponse(response);
    }
}
