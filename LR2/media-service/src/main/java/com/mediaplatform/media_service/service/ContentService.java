package com.mediaplatform.media_service.service;

import com.mediaplatform.media_service.dto.ContentEditRequest;
import com.mediaplatform.media_service.dto.ModerationDecisionRequest;
import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.model.Content;
import com.mediaplatform.media_service.model.ContentEdit;
import com.mediaplatform.media_service.model.Moderation;
import com.mediaplatform.media_service.repository.ContentEditRepository;
import com.mediaplatform.media_service.repository.ContentRepository;
import com.mediaplatform.media_service.repository.ModerationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ContentService {
    private final ContentRepository repository;
    private final ContentEditRepository editRepository;
    private final ModerationRepository moderationRepository;

    // In-memory рейтинги по contentId
    private final ConcurrentMap<Long, CopyOnWriteArrayList<Integer>> ratingsByContentId = new ConcurrentHashMap<>();

    public ContentService(ContentRepository repository,
                          ContentEditRepository editRepository,
                          ModerationRepository moderationRepository) {
        this.repository = repository;
        this.editRepository = editRepository;
        this.moderationRepository = moderationRepository;
    }

    public List<Content> getAllContents() {
        return repository.findAll();
    }

    public List<Content> getContentsByCategoryId(Long categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    public Optional<Content> getById(Long id) {
        return repository.findById(id);
    }

    public Content createContent(Content content) {
        content.setId(repository.nextId());
        content.setStatus("PENDING"); // Початковий статус для модерації
        return repository.save(content.getId(), content);
    }

    public Content publish(Long contentId) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + contentId + " не знайдено"));
        content.setStatus("PUBLISHED");
        repository.save(content.getId(), content);
        return content;
    }

    public Content edit(Long contentId, ContentEditRequest request) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + contentId + " не знайдено"));

        ContentEdit edit = new ContentEdit();
        edit.setId(editRepository.nextId());
        edit.setContentId(contentId);
        edit.setOldTitle(content.getTitle());
        edit.setOldBody(content.getBody());
        edit.setNewTitle(request.getTitle());
        edit.setNewBody(request.getBody());
        editRepository.save(edit.getId(), edit);

        content.setTitle(request.getTitle());
        content.setBody(request.getBody());
        repository.save(content.getId(), content);

        return content;
    }

    public List<ContentEdit> getHistory(Long contentId) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + contentId + " не знайдено"));
        return editRepository.findByContentId(content.getId());
    }

    public Content moderate(Long contentId, ModerationDecisionRequest request) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + contentId + " не знайдено"));

        String resolution = request.getResolution().trim().toUpperCase();
        if (!resolution.equals("APPROVED") && !resolution.equals("REJECTED")) {
            throw new IllegalArgumentException("resolution має бути APPROVED або REJECTED");
        }

        Moderation moderation = new Moderation();
        moderation.setId(moderationRepository.nextId());
        moderation.setContentId(contentId);
        moderation.setResolution(resolution);
        moderation.setReason(request.getReason());
        moderationRepository.save(moderation.getId(), moderation);

        if (resolution.equals("APPROVED")) {
            content.setStatus("PUBLISHED");
        } else {
            content.setStatus("BLOCKED");
        }
        repository.save(content.getId(), content);

        return content;
    }

    // Бізнес-логіка: коректний підрахунок середнього рейтингу
    public Content addRating(Long contentId, Integer score) {
        Content content = repository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + contentId + " не знайдено"));

        ratingsByContentId.computeIfAbsent(contentId, k -> new CopyOnWriteArrayList<>()).add(score);

        double avg = ratingsByContentId.get(contentId).stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        content.setAverageRating(avg);
        repository.save(content.getId(), content);
        return content;
    }
}