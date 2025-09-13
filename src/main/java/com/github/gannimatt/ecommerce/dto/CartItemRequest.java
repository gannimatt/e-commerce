package com.github.gannimatt.ecommerce.dto;
import jakarta.validation.constraints.Min; import jakarta.validation.constraints.NotNull;

public record CartItemRequest(@NotNull Long productId, @Min(1) int quantity) {}