package com.apiintegration.hngstage1profileaggregator.utils;

import java.util.Map;

public class SortFieldMapper {

    private static final Map<String, String> SORT_FIELD_MAP = Map.of(
            "gender_probability", "genderProbability",
            "created_at", "createdAt",
            "age", "age"
    );

    public static String getSortField(String sortBy) {
        if (sortBy == null) {
            return "createdAt";
        }
        return SORT_FIELD_MAP.getOrDefault(sortBy, "createdAt");
    }
}
