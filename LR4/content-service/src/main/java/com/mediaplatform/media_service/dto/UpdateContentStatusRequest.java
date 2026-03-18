package com.mediaplatform.media_service.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateContentStatusRequest(
        @NotBlank(message = "status is required")
        String status
) {
}
