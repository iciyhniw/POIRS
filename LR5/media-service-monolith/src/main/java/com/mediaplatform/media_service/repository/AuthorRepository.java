package com.mediaplatform.media_service.repository;

import com.mediaplatform.media_service.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}