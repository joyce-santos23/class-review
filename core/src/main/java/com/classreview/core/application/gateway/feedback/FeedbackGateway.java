package com.classreview.core.application.gateway.feedback;

import com.classreview.core.domain.entity.Feedback;

import java.time.Instant;
import java.util.List;

public interface FeedbackGateway {
    void save(Feedback feedback);
    List<Feedback> findByPeriod(Instant start, Instant end);
}
