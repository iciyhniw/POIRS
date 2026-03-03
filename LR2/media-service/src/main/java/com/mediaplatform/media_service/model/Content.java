package com.mediaplatform.media_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class Content {
    private Long id;

    @NotBlank(message = "Заголовок обов'язковий")
    @Size(min = 5, message = "Заголовок занадто короткий")
    private String title;

    @NotBlank(message = "Текст не може бути порожнім")
    private String body;

    @NotNull(message = "Автор має бути вказаний")
    private Long authorId;

    @NotNull(message = "Категорія має бути вказана")
    private Long categoryId;

    private String status; // PENDING, PUBLISHED, BLOCKED
    private Double averageRating = 0.0;
    private LocalDateTime createdAt = LocalDateTime.now();
}

