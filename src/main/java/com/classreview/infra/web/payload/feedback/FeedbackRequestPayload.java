package com.classreview.infra.web.payload.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record FeedbackRequestPayload(

        @Min(0)
        @Max(10)
        int rating,

        @NotBlank
        String comment
) {
}
