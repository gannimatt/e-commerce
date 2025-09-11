package com.github.gannimatt.ecommerce.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank String sku,
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin(value="0.0", inclusive=false) BigDecimal price,
        Long categoryId
) {}
