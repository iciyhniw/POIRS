package com.mediaplatform.media_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 2. Автор (User)
@Data @AllArgsConstructor @NoArgsConstructor
public class Author {
    private Long id;
    private String username;
    private String email;
}
