package com.classreview.infra.web.mapper;

import com.classreview.core.application.dto.feedback.FeedbackRequestDTO;
import com.classreview.core.application.dto.feedback.FeedbackResponseDTO;
import com.classreview.infra.web.payload.FeedbackRequestPayload;
import com.classreview.infra.web.payload.FeedbackResponsePayload;

public class FeedbackWebMapper {

    public static FeedbackRequestDTO toDTO(FeedbackRequestPayload payload) {
        return new FeedbackRequestDTO(
                payload.rating(),
                payload.comment()
        );
    }

    public static FeedbackResponsePayload toResponse(FeedbackResponseDTO payload) {
        return new FeedbackResponsePayload(
                payload.id(),
                payload.rating(),
                payload.comment(),
                payload.urgency()
        );
    }
}
