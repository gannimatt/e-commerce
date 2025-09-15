package com.github.gannimatt.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 120, message = "Name must not exceed 120 characters")
        String name,

        @NotBlank(message = "Slug is required")
        @Size(max = 120, message = "Slug must not exceed 120 characters")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "Slug must be lower-kebab-case (letters/digits and hyphens)"
        )
        String slug,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description
) {}

