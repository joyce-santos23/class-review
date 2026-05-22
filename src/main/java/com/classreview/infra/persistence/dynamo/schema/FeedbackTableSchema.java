package com.classreview.infra.persistence.dynamo.schema;

import com.classreview.infra.persistence.dynamo.entity.FeedbackEntity;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;

public class FeedbackTableSchema {

    private FeedbackTableSchema() {}

    public static final TableSchema<FeedbackEntity> SCHEMA =
            TableSchema.builder(FeedbackEntity.class)

                    .newItemSupplier(FeedbackEntity::new)

                    .addAttribute(
                            String.class,
                            a -> a.name("id")
                                    .getter(FeedbackEntity::getId)
                                    .setter(FeedbackEntity::setId)
                                    .tags(StaticAttributeTags.primaryPartitionKey())
                    )

                    .addAttribute(
                            Integer.class,
                            a -> a.name("rating")
                                    .getter(FeedbackEntity::getRating)
                                    .setter(FeedbackEntity::setRating)
                    )

                    .addAttribute(
                            String.class,
                            a -> a.name("comment")
                                    .getter(FeedbackEntity::getComment)
                                    .setter(FeedbackEntity::setComment)
                    )

                    .addAttribute(
                            String.class,
                            a -> a.name("urgency")
                                    .getter(FeedbackEntity::getUrgency)
                                    .setter(FeedbackEntity::setUrgency)
                    )

                    .addAttribute(
                            String.class,
                            a -> a.name("createdAt")
                                    .getter(FeedbackEntity::getCreatedAt)
                                    .setter(FeedbackEntity::setCreatedAt)
                    )

                    .build();
}