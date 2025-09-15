package com.github.gannimatt.ecommerce.repository;

import com.github.gannimatt.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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


    long countByCategoryId(Long categoryId);

    @Query("""
    SELECT p FROM Product p
    WHERE (:q IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :q, '%')))
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
      AND (:minPrice IS NULL OR p.price >= :minPrice)
      AND (:maxPrice IS NULL OR p.price <= :maxPrice)
""")
    Page<Product> searchProducts(@Param("q") String q,
                                 @Param("categoryId") Long categoryId,
                                 @Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice,
                                 Pageable pageable);



}
