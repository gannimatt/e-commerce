package com.github.gannimatt.ecommerce.repository;

import com.github.gannimatt.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);
    boolean existsByNameIgnoreCase(String name);
}
