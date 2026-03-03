package com.mediaplatform.media_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 5. Рейтинг
@Data @AllArgsConstructor @NoArgsConstructor
public class Rating {
    private Long id;
    private Long contentId;
    private Long authorId;
    private Integer score; // 1-5
}
