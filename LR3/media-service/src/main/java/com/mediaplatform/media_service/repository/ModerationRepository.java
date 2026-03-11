package com.mediaplatform.media_service.repository;

import com.mediaplatform.media_service.model.Moderation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModerationRepository extends JpaRepository<Moderation, Long> {
}