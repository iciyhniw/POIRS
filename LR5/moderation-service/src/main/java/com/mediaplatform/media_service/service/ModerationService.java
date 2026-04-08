package com.mediaplatform.media_service.service;

import com.mediaplatform.media_service.dto.ModerationDecisionRequest;
import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.integration.ContentServiceClient;
import com.mediaplatform.media_service.model.Moderation;
import com.mediaplatform.media_service.repository.ModerationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModerationService {

    private final ModerationRepository repository;
    private final ContentServiceClient contentServiceClient;

    public ModerationService(ModerationRepository repository, ContentServiceClient contentServiceClient) {
        this.repository = repository;
        this.contentServiceClient = contentServiceClient;
    }

    public List<Moderation> getAll() {
        return repository.findAll();
    }

    public Optional<Moderation> getById(Long id) {
        return repository.findById(id);
    }

    public Moderation moderate(Long contentId, ModerationDecisionRequest request) {
        contentServiceClient.ensureContentExists(contentId);

        String resolution = request.resolution() == null ? "" : request.resolution().trim().toUpperCase();
        if (!resolution.equals("APPROVED") && !resolution.equals("REJECTED")) {
            throw new IllegalArgumentException("resolution must be APPROVED or REJECTED");
        }

        Moderation moderation = new Moderation();
        moderation.setId(null);
        moderation.setContentId(contentId);
        moderation.setResolution(resolution);
        moderation.setReason(request.reason());

        Moderation saved = repository.save(moderation);
        contentServiceClient.updateStatus(contentId, resolution.equals("APPROVED") ? "PUBLISHED" : "BLOCKED");

        return saved;
    }

    public Moderation requireById(Long id) {
        return getById(id).orElseThrow(() -> new EntityNotFoundException("Moderation with id " + id + " was not found"));
    }
}
