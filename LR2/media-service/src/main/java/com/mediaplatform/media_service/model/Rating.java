package com.mediaplatform.media_service.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 5. Рейтинг
@Data @AllArgsConstructor @NoArgsConstructor
public class Rating {
    private Long id;

    @NotNull(message = "contentId обов'язковий")
    private Long contentId;

    @NotNull(message = "authorId обов'язковий")
    private Long authorId;

    @NotNull(message = "score обов'язковий")
    @Min(value = 1, message = "score має бути 1..5")
    @Max(value = 5, message = "score має бути 1..5")
    private Integer score; // 1-5
}