package com.github.gannimatt.ecommerce.dto;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description
) {}
