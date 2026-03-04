package com.mediaplatform.media_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 6. Модерація
@Data @AllArgsConstructor @NoArgsConstructor
public class Moderation {
    private Long id;

    @NotNull(message = "contentId обов'язковий")
    private Long contentId;

    @NotBlank(message = "resolution обов'язковий")
    private String resolution; // APPROVED, REJECTED

    private String reason;
    private LocalDateTime moderatedAt = LocalDateTime.now();
}