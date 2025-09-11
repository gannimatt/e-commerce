package com.github.gannimatt.ecommerce.service.impl;

import com.github.gannimatt.ecommerce.dto.CategoryRequest;
import com.github.gannimatt.ecommerce.dto.CategoryResponse;
import com.github.gannimatt.ecommerce.entity.Category;
import com.github.gannimatt.ecommerce.exception.NotFoundException;
import com.github.gannimatt.ecommerce.mapper.CategoryMapper;
import com.github.gannimatt.ecommerce.repository.CategoryRepository;
import com.github.gannimatt.ecommerce.repository.ProductRepository;
import com.github.gannimatt.ecommerce.service.CategoryService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        validateUnique(request.name(), request.slug(), null);
        Category saved = categoryRepository.save(CategoryMapper.toEntity(request));
        return CategoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        return CategoryMapper.toResponse(c);
    }

    @Override
    public CategoryResponse getBySlug(String slug) {
        Category c = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Category not found by slug: " + slug));
        return CategoryMapper.toResponse(c);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        validateUnique(request.name(), request.slug(), id);
        CategoryMapper.updateEntity(existing, request);
        Category saved = categoryRepository.save(existing);
        return CategoryMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));

        // Optional guard: prevent deleting if products exist in this category
        long count = productRepository.countByCategoryId(existing.getId());
        if (count > 0) {
            throw new DataIntegrityViolationException("Cannot delete category with existing products");
        }

        categoryRepository.delete(existing);
    }

    @Override
    public Page<CategoryResponse> search(String q, Pageable pageable) {
        Page<Category> page;
        if (StringUtils.hasText(q)) {
            String like = "%" + q.toLowerCase() + "%";
            page = categoryRepository.search(like, pageable);
        } else {
            page = categoryRepository.findAll(pageable);
        }
        return page.map(CategoryMapper::toResponse);
    }

    private void validateUnique(String name, String slug, Long currentId) {
        boolean nameExists = categoryRepository.existsByNameIgnoreCase(name)
                && (currentId == null || !categoryRepository.findById(currentId)
                .map(c -> c.getName().equalsIgnoreCase(name)).orElse(false));
        if (nameExists) {
            throw new DataIntegrityViolationException("Category name already exists");
        }

        boolean slugExists = categoryRepository.existsBySlugIgnoreCase(slug)
                && (currentId == null || !categoryRepository.findById(currentId)
                .map(c -> c.getSlug().equalsIgnoreCase(slug)).orElse(false));
        if (slugExists) {
            throw new DataIntegrityViolationException("Category slug already exists");
        }
    }
}
