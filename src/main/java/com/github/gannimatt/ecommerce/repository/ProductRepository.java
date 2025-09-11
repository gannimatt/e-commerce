package com.github.gannimatt.ecommerce.repository;

import com.github.gannimatt.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    // ProductRepository
    @EntityGraph(attributePaths = "category")
    @Query("select p from Product p")
    Page<Product> findAllWithCategory(Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
            String name, String sku, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<Product> findByCategory_Id(Long categoryId, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<Product> findByCategory_IdAndNameContainingIgnoreCaseOrCategory_IdAndSkuContainingIgnoreCase(
            Long cat1, String name, Long cat2, String sku, Pageable pageable);


}
