package com.mediaplatform.media_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 3. Категорія
@Data @AllArgsConstructor @NoArgsConstructor
public class Category {
    private Long id;
    private String name;
}
