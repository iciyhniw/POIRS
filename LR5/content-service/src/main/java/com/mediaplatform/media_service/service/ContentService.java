package com.mediaplatform.media_service.service;

import com.mediaplatform.media_service.dto.ContentEditRequest;
import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.model.Content;
import com.mediaplatform.media_service.model.ContentEdit;
import com.mediaplatform.media_service.repository.ContentEditRepository;
import com.mediaplatform.media_service.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContentService {
    private final ContentRepository repository;
    private final ContentEditRepository editRepository;
    private final long cacheMissDelayMs;

    public ContentService(ContentRepository repository,
                          ContentEditRepository editRepository,
                          @Value("${app.cache.demo-delay-ms:0}") long cacheMissDelayMs) {
        this.repository = repository;
        this.editRepository = editRepository;
        this.cacheMissDelayMs = Math.max(cacheMissDelayMs, 0);
    }

    public List<Content> getAllContents(Long categoryId,
                                        Integer page,
                                        Integer size,
                                        String sortBy,
                                        String direction) {

        Sort sort = buildSort(sortBy, direction);

        Pageable pageable;
        if (page == null || size == null) {
            pageable = Pageable.unpaged(sort);
        } else {
            if (page < 0) {
                throw new IllegalArgumentException("page must be >= 0");
            }
            if (size <= 0) {
                throw new IllegalArgumentException("size must be > 0");
            }
            pageable = PageRequest.of(page, size, sort);
        }

        if (categoryId != null) {
            return repository.findByCategoryId(categoryId, pageable).getContent();
        }
        return repository.findAll(pageable).getContent();
    }

    @Cacheable(cacheNames = "contentById", key = "#id")
    public Content getById(Long id) {
        applyDemoDelay();
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Content with id " + id + " was not found"));
    }

    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @CacheEvict(cacheNames = "contentById", key = "#result.id", condition = "#result != null")
    public Content createContent(Content content) {
        content.setId(null);
        return repository.save(content);
    }

    @CacheEvict(cacheNames = "contentById", key = "#id")
    public Content update(Long id, Content content) {
        Content existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Content with id " + id + " was not found"));

        content.setId(id);
        if (content.getStatus() == null) {
            content.setStatus(existing.getStatus());
        }
        if (content.getAverageRating() == null) {
            content.setAverageRating(existing.getAverageRating());
        }
        if (content.getCreatedAt() == null) {
            content.setCreatedAt(existing.getCreatedAt());
        }

        return repository.save(content);
    }

    @CacheEvict(cacheNames = "contentById", key = "#id")
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Content with id " + id + " was not found");
        }
        repository.deleteById(id);
    }

    @Transactional
    @CacheEvict(cacheNames = "contentById", key = "#contentId")
    public Content publish(Long contentId) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content with id " + contentId + " was not found"));
        content.setStatus("PUBLISHED");
        return repository.save(content);
    }

    @Transactional
    @CacheEvict(cacheNames = "contentById", key = "#contentId")
    public Content edit(Long contentId, ContentEditRequest request) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content with id " + contentId + " was not found"));

        ContentEdit edit = new ContentEdit();
        edit.setContentId(contentId);
        edit.setOldTitle(content.getTitle());
        edit.setOldBody(content.getBody());
        edit.setNewTitle(request.getTitle());
        edit.setNewBody(request.getBody());
        editRepository.save(edit);

        content.setTitle(request.getTitle());
        content.setBody(request.getBody());
        return repository.save(content);
    }

    public List<ContentEdit> getHistory(Long contentId) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content with id " + contentId + " was not found"));
        return editRepository.findByContentId(content.getId());
    }

    @Transactional
    @CacheEvict(cacheNames = "contentById", key = "#contentId")
    public Content updateStatus(Long contentId, String status) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content with id " + contentId + " was not found"));

        String normalizedStatus = status == null ? "" : status.trim().toUpperCase();
        if (!normalizedStatus.equals("PENDING")
                && !normalizedStatus.equals("PUBLISHED")
                && !normalizedStatus.equals("BLOCKED")) {
            throw new IllegalArgumentException("status must be PENDING, PUBLISHED or BLOCKED");
        }

        content.setStatus(normalizedStatus);
        return repository.save(content);
    }

    @Transactional
    @CacheEvict(cacheNames = "contentById", key = "#contentId")
    public Content updateAverageRating(Long contentId, Double averageRating) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content with id " + contentId + " was not found"));

        content.setAverageRating(averageRating == null ? 0.0 : averageRating);
        return repository.save(content);
    }

    private Sort buildSort(String sortBy, String direction) {
        if (sortBy == null || sortBy.isBlank()) {
            return Sort.unsorted();
        }

        Sort.Direction dir = (direction != null && direction.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return switch (sortBy) {
            case "createdAt", "averageRating", "title" -> Sort.by(dir, sortBy);
            default -> throw new IllegalArgumentException(
                    "Unknown sortBy field: " + sortBy + " (allowed: createdAt, averageRating, title)"
            );
        };
    }

    private void applyDemoDelay() {
        if (cacheMissDelayMs <= 0) {
            return;
        }

        try {
            Thread.sleep(cacheMissDelayMs);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread was interrupted while simulating cache miss delay", ex);
        }
    }
}
