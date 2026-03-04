package com.mediaplatform.media_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class ContentEdit {
    private Long id;
    private Long contentId;

    private String oldTitle;
    private String oldBody;

    private String newTitle;
    private String newBody;

    private LocalDateTime editedAt = LocalDateTime.now();
}