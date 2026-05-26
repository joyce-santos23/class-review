package com.persistence.dynamo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedbackEntity {

    private String id;
    private int rating;
    private String comment;
    private String urgency;
    private String createdAt;

}
