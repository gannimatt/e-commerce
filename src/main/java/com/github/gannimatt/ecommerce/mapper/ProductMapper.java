package com.github.gannimatt.ecommerce.mapper;

import com.github.gannimatt.ecommerce.dto.ProductRequest;
import com.github.gannimatt.ecommerce.dto.ProductResponse;
import com.github.gannimatt.ecommerce.entity.Product;

public final class ProductMapper {
    private ProductMapper() {}

    public static Product toEntity(ProductRequest r) {
        return Product.builder()
                .sku(r.sku()).name(r.name()).description(r.description()).price(r.price())
                .build();
    } //category will be added in the service after loading the category from DB.

    public static ProductResponse toResponse(Product p) {
        Long categoryId = (p.getCategory() != null) ? p.getCategory().getId() : null;
        String categoryName = (p.getCategory() != null) ? p.getCategory().getName() : null;
        return new ProductResponse(
                p.getId(),
                p.getSku(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                categoryId,
                categoryName
        );
    }
}
