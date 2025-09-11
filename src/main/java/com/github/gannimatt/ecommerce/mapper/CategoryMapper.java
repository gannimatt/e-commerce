package com.github.gannimatt.ecommerce.mapper;

import com.github.gannimatt.ecommerce.dto.CategoryRequest;
import com.github.gannimatt.ecommerce.dto.CategoryResponse;
import com.github.gannimatt.ecommerce.entity.Category;

public final class CategoryMapper {

    private CategoryMapper() {}

    public static Category toEntity(CategoryRequest req) {
        Category c = new Category();
        c.setName(req.name());
        c.setSlug(req.slug());
        c.setDescription(req.description());
        return c;
    }

    public static void updateEntity(Category entity, CategoryRequest req) {
        entity.setName(req.name());
        entity.setSlug(req.slug());
        entity.setDescription(req.description());
    }

    public static CategoryResponse toResponse(Category c) {
        return new CategoryResponse(
                c.getId(),
                c.getName(),
                c.getSlug(),
                c.getDescription()
        );
    }
}
