package com.github.gannimatt.ecommerce.repository;

import com.github.gannimatt.ecommerce.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ProductRepositoryIT {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void save_and_findBySku_shouldPersistToH2() {
        Product p = Product.builder()
                .sku("IT-SKU-1")
                .name("Integration Test Product")
                .description("Test")
                .price(new BigDecimal("12.34"))
                .build();

        Product saved = productRepository.save(p);

        assertNotNull(saved.getId());

        Optional<Product> reloaded = productRepository.findBySku("IT-SKU-1");
        assertTrue(reloaded.isPresent());
        assertEquals("Integration Test Product", reloaded.get().getName());
        assertEquals(new BigDecimal("12.34"), reloaded.get().getPrice());
    }
}


