package com.mediaplatform.media_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RatingCreateRequest(
        @NotNull(message = "authorId is required")
        Long authorId,

        @NotNull(message = "score is required")
        @Min(value = 1, message = "score must be in range 1..5")
        @Max(value = 5, message = "score must be in range 1..5")
        Integer score
) {
}
