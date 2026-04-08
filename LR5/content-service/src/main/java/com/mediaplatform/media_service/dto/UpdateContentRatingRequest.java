package com.mediaplatform.media_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record UpdateContentRatingRequest(
        @NotNull(message = "averageRating is required")
        @DecimalMin(value = "0.0", message = "averageRating must be >= 0")
        Double averageRating
) {
}
