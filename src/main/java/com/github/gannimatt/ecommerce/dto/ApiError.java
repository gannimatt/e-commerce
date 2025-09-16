package com.github.gannimatt.ecommerce.dto;

import java.time.Instant;
import java.util.List;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldError> details
) {
    public record FieldError(String field, String message) {}
}
