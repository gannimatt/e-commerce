package com.github.gannimatt.ecommerce.service;

import com.github.gannimatt.ecommerce.dto.CategoryRequest;
import com.github.gannimatt.ecommerce.dto.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse getById(Long id);

    CategoryResponse getBySlug(String slug);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);

    Page<CategoryResponse> search(String q, Pageable pageable);
}
