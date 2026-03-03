package com.mediaplatform.media_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 4. Коментар
@Data @AllArgsConstructor @NoArgsConstructor
public class Comment {
    private Long id;
    private Long contentId;
    private Long authorId;
    private String text;
    private LocalDateTime createdAt = LocalDateTime.now();
}
