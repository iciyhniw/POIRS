package com.mediaplatform.media_service.service;

import com.mediaplatform.media_service.dto.ContentEditRequest;
import com.mediaplatform.media_service.dto.ModerationDecisionRequest;
import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.model.Content;
import com.mediaplatform.media_service.model.ContentEdit;
import com.mediaplatform.media_service.model.Moderation;
import com.mediaplatform.media_service.model.Rating;
import com.mediaplatform.media_service.repository.ContentEditRepository;
import com.mediaplatform.media_service.repository.ContentRepository;
import com.mediaplatform.media_service.repository.ModerationRepository;
import com.mediaplatform.media_service.repository.RatingRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContentService {
    private final ContentRepository repository;
    private final ContentEditRepository editRepository;
    private final ModerationRepository moderationRepository;
    private final RatingRepository ratingRepository;

    public ContentService(ContentRepository repository,
                          ContentEditRepository editRepository,
                          ModerationRepository moderationRepository,
                          RatingRepository ratingRepository) {
        this.repository = repository;
        this.editRepository = editRepository;
        this.moderationRepository = moderationRepository;
        this.ratingRepository = ratingRepository;
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
            if (page < 0) throw new IllegalArgumentException("page має бути >= 0");
            if (size <= 0) throw new IllegalArgumentException("size має бути > 0");
            pageable = PageRequest.of(page, size, sort);
        }

        if (categoryId != null) {
            return repository.findByCategoryId(categoryId, pageable).getContent();
        }
        return repository.findAll(pageable).getContent();
    }

    public Optional<Content> getById(Long id) {
        return repository.findById(id);
    }

    public Content createContent(Content content) {
        content.setId(null);
        return repository.save(content);
    }

    public Content update(Long id, Content content) {
        Content existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + id + " не знайдено"));

        content.setId(id);
        if (content.getStatus() == null) content.setStatus(existing.getStatus());
        if (content.getAverageRating() == null) content.setAverageRating(existing.getAverageRating());
        if (content.getCreatedAt() == null) content.setCreatedAt(existing.getCreatedAt());

        return repository.save(content);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Контент з ID " + id + " не знайдено");
        }
        repository.deleteById(id);
    }

    @Transactional
    public Content publish(Long contentId) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + contentId + " не знайдено"));
        content.setStatus("PUBLISHED");
        return repository.save(content);
    }

    @Transactional
    public Content edit(Long contentId, ContentEditRequest request) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + contentId + " не знайдено"));

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
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + contentId + " не знайдено"));
        return editRepository.findByContentId(content.getId());
    }

    @Transactional
    public Content moderate(Long contentId, ModerationDecisionRequest request) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + contentId + " не знайдено"));

        String resolution = request.getResolution().trim().toUpperCase();
        if (!resolution.equals("APPROVED") && !resolution.equals("REJECTED")) {
            throw new IllegalArgumentException("resolution має бути APPROVED або REJECTED");
        }

        Moderation moderation = new Moderation();
        moderation.setContentId(contentId);
        moderation.setResolution(resolution);
        moderation.setReason(request.getReason());
        moderationRepository.save(moderation);

        content.setStatus(resolution.equals("APPROVED") ? "PUBLISHED" : "BLOCKED");
        return repository.save(content);
    }

    @Transactional
    public Content addRating(Long contentId, Integer score) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + contentId + " не знайдено"));

        Rating rating = new Rating();
        rating.setId(null);
        rating.setContentId(contentId);
        rating.setAuthorId(content.getAuthorId()); // спрощення для LR: беремо authorId з контенту
        rating.setScore(score);
        ratingRepository.save(rating);

        Double avg = ratingRepository.findAverageScoreByContentId(contentId);
        content.setAverageRating(avg == null ? 0.0 : avg);

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
                    "Невідоме поле sortBy: " + sortBy + " (доступно: createdAt, averageRating, title)"
            );
        };
    }
}