package com.classreview.core.domain.enums;

public enum UrgencyLevel {
    CRITICAL,
    MEDIUM,
    GOOD;

    public static UrgencyLevel fromRating(int rating) {
        if (rating <= 4) return CRITICAL;
        if (rating <= 7) return MEDIUM;
        return GOOD;
    }
}
