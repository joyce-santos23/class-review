package com.web.mapper.feedback;

import com.classreview.core.application.dto.feedback.FeedbackRequestDTO;
import com.classreview.core.application.dto.feedback.FeedbackResponseDTO;
import com.web.payload.feedback.FeedbackRequestPayload;
import com.web.payload.feedback.FeedbackResponsePayload;

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
