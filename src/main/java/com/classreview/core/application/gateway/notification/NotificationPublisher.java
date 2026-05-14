package com.classreview.core.application.gateway.notification;

import com.classreview.core.application.dto.event.FeedbackEventDTO;

public interface NotificationPublisher {
    void publishCriticalFeedback(FeedbackEventDTO feedback);
}
