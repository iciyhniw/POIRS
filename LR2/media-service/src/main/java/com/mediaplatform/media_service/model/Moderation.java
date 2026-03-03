package com.mediaplatform.media_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 6. Модерація
@Data @AllArgsConstructor @NoArgsConstructor
public class Moderation {
    private Long id;
    private Long contentId;
    private String resolution; // APPROVED, REJECTED
    private String reason;
    private LocalDateTime moderatedAt = LocalDateTime.now();
}
