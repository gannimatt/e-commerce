package com.github.gannimatt.ecommerce.repository;

import com.github.gannimatt.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
}
