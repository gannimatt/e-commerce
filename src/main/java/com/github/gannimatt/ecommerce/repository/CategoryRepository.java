package com.github.gannimatt.ecommerce.repository;

import com.github.gannimatt.ecommerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);
    boolean existsBySlugIgnoreCase(String slug);
    boolean existsByNameIgnoreCase(String name);

    @Query("""
           SELECT c FROM Category c
           WHERE LOWER(c.name) LIKE :like
              OR LOWER(c.slug) LIKE :like
           """)
    Page<Category> search(String like, Pageable pageable);
}
