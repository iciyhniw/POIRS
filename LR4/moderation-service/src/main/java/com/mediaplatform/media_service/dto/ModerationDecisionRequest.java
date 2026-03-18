package com.mediaplatform.media_service.dto;

import jakarta.validation.constraints.NotBlank;

public record ModerationDecisionRequest(
        @NotBlank(message = "resolution is required")
        String resolution,

        String reason
) {
}
