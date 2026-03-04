package com.mediaplatform.media_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 4. Коментар
@Data @AllArgsConstructor @NoArgsConstructor
public class Comment {
    private Long id;

    @NotNull(message = "contentId обов'язковий")
    private Long contentId;

    @NotNull(message = "authorId обов'язковий")
    private Long authorId;

    @NotBlank(message = "text не може бути порожнім")
    @Size(min = 1, max = 500, message = "text має бути 1..500 символів")
    private String text;

    private LocalDateTime createdAt = LocalDateTime.now();
}