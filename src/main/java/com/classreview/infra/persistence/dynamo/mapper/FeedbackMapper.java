package com.classreview.infra.persistence.dynamo.mapper;

import com.classreview.core.domain.entity.Feedback;
import com.classreview.core.domain.enums.UrgencyLevel;
import com.classreview.infra.persistence.dynamo.entity.FeedbackEntity;

import java.time.Instant;

public class FeedbackMapper {

    public static FeedbackEntity toEntity(Feedback feedback) {
        FeedbackEntity entity = new FeedbackEntity();
        entity.setId(feedback.getId());
        entity.setRating(feedback.getRating());
        entity.setComment(feedback.getComment());
        entity.setUrgency(feedback.getUrgency().name());
        entity.setCreatedAt(feedback.getCreatedAt().toString());
        return entity;
    }

    public static Feedback toDomain(FeedbackEntity entity) {
        return Feedback.restore(
                entity.getId(),
                entity.getRating(),
                entity.getComment(),
                Instant.parse(entity.getCreatedAt()),
                UrgencyLevel.valueOf(entity.getUrgency())
        );
    }
}
