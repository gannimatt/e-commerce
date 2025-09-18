package com.github.gannimatt.ecommerce.mapper;

import com.github.gannimatt.ecommerce.dto.ProductRequest;
import com.github.gannimatt.ecommerce.dto.ProductResponse;
import com.github.gannimatt.ecommerce.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void toEntity_shouldMapSimpleFields() {
        ProductRequest req = new ProductRequest("SKU-1", "Name", "Desc", new BigDecimal("9.99"), null);
        Product entity = ProductMapper.toEntity(req);
        assertEquals("SKU-1", entity.getSku());
        assertEquals("Name", entity.getName());
        assertEquals("Desc", entity.getDescription());
        assertEquals(new BigDecimal("9.99"), entity.getPrice());
        assertNull(entity.getCategory());
    }

    @Test
    void toResponse_shouldHandleNullCategory() {
        Product p = Product.builder()
                .id(1L).sku("S").name("N").description("D").price(new BigDecimal("1.00"))
                .build();
        ProductResponse resp = ProductMapper.toResponse(p);
        assertEquals(1L, resp.id());
        assertNull(resp.categoryId());
        assertNull(resp.categoryName());
    }
}


