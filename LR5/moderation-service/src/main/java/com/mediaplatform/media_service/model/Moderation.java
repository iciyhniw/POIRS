package com.mediaplatform.media_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "moderations")
@Data @AllArgsConstructor @NoArgsConstructor
public class Moderation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "contentId обов'язковий")
    @Column(nullable = false)
    private Long contentId;

    @NotBlank(message = "resolution обов'язковий")
    @Column(nullable = false)
    private String resolution; // APPROVED, REJECTED

    private String reason;

    @Column(nullable = false)
    private LocalDateTime moderatedAt;

    @PrePersist
    void onCreate() {
        if (moderatedAt == null) moderatedAt = LocalDateTime.now();
    }
}