package com.web.controller.feedback;

import com.classreview.core.application.dto.feedback.FeedbackResponseDTO;
import com.classreview.core.application.usecase.feedback.CreateFeedbackUseCase;
import com.web.mapper.feedback.FeedbackWebMapper;
import com.web.payload.feedback.FeedbackRequestPayload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    public Response createFeedback(@Valid FeedbackRequestPayload request) {

        FeedbackResponseDTO response = createFeedbackUseCase.execute(FeedbackWebMapper.toDTO(request));
        return Response.status(Response.Status.CREATED)
                .entity(FeedbackWebMapper.toResponse(response))
                .build();
    }
}
