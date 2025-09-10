package com.github.gannimatt.ecommerce.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id, String sku, String name, String description, BigDecimal price
) {}
