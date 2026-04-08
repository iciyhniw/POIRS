package com.mediaplatform.media_service.service;

import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.model.Category;
import com.mediaplatform.media_service.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAll() {
        return repository.findAll();
    }

    public Optional<Category> getById(Long id) {
        return repository.findById(id);
    }

    public Category create(Category category) {
        category.setId(null);
        return repository.save(category);
    }

    public Category update(Long id, Category category) {
        requireById(id);
        category.setId(id);
        return repository.save(category);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Категорія з ID " + id + " не знайдена");
        }
        repository.deleteById(id);
    }

    public Category requireById(Long id) {
        return getById(id).orElseThrow(() -> new EntityNotFoundException("Категорія з ID " + id + " не знайдена"));
    }
}