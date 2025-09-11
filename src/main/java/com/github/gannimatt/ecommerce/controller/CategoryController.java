package com.github.gannimatt.ecommerce.controller;

import com.github.gannimatt.ecommerce.dto.CategoryRequest;
import com.github.gannimatt.ecommerce.dto.CategoryResponse;
import com.github.gannimatt.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public Page<CategoryResponse> list(@RequestParam(required = false) String q, Pageable pageable) {
        return service.search(q, pageable);
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/slug/{slug}")
    public CategoryResponse getBySlug(@PathVariable String slug) {
        return service.getBySlug(slug);
    }

    @PostMapping
    public CategoryResponse create(@Valid @RequestBody CategoryRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
