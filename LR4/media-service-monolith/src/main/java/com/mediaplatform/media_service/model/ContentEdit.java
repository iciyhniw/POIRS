package com.mediaplatform.media_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "content_edits")
@Data @AllArgsConstructor @NoArgsConstructor
public class ContentEdit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long contentId;

    @Column(columnDefinition = "TEXT")
    private String oldTitle;

    @Column(columnDefinition = "TEXT")
    private String oldBody;

    @Column(columnDefinition = "TEXT")
    private String newTitle;

    @Column(columnDefinition = "TEXT")
    private String newBody;

    @Column(nullable = false)
    private LocalDateTime editedAt;

    @PrePersist
    void onCreate() {
        if (editedAt == null) editedAt = LocalDateTime.now();
    }
}