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
    }

    public static ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getSku(), p.getName(), p.getDescription(), p.getPrice());
    }
}
