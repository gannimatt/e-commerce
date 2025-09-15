package com.github.gannimatt.ecommerce.service.impl;

import com.github.gannimatt.ecommerce.dto.ProductRequest;
import com.github.gannimatt.ecommerce.dto.ProductResponse;
import com.github.gannimatt.ecommerce.entity.Category;
import com.github.gannimatt.ecommerce.entity.Product;
import com.github.gannimatt.ecommerce.exception.NotFoundException;
import com.github.gannimatt.ecommerce.mapper.ProductMapper;
import com.github.gannimatt.ecommerce.repository.CategoryRepository;
import com.github.gannimatt.ecommerce.repository.ProductRepository;
import com.github.gannimatt.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.github.gannimatt.ecommerce.mapper.ProductMapper.toEntity;
import static com.github.gannimatt.ecommerce.mapper.ProductMapper.toResponse;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository repo, CategoryRepository categoryRepository) {
        this.repo = repo;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductResponse> getAll() {
        return repo.findAll().stream().map(ProductMapper::toResponse).toList();
    }

    @Override
    public ProductResponse getById(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product " + id + " not found"));
        return toResponse(p);
    }

    @Override
    public ProductResponse create(ProductRequest req) {
        // unique SKU
        repo.findBySku(req.sku()).ifPresent(p -> { throw new IllegalArgumentException("SKU already exists"); });

        // map request -> entity
        Product entity = toEntity(req);

        // set category if provided
        if (req.categoryId() != null) {
            Category cat = categoryRepository.findById(req.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.categoryId()));
            entity.setCategory(cat);
        }

        // save and return
        Product saved = repo.save(entity);
        return toResponse(saved);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest req) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product " + id + " not found"));

        // if sku changed, enforce uniqueness
        if (!p.getSku().equals(req.sku()) && repo.findBySku(req.sku()).isPresent()) {
            throw new IllegalArgumentException("SKU already exists");
        }

        // update fields
        p.setSku(req.sku());
        p.setName(req.name());
        p.setDescription(req.description());
        p.setPrice(req.price());

        // update category (null clears it)
        if (req.categoryId() == null) {
            p.setCategory(null);
        } else {
            Category cat = categoryRepository.findById(req.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.categoryId()));
            p.setCategory(cat);
        }

        return toResponse(repo.save(p));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new NotFoundException("Product " + id + " not found");
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> search(String q, Pageable pageable, Long categoryId,
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Category not found: " + categoryId);
        }
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("minPrice cannot be greater than maxPrice");
        }

        return repo.searchProducts(q, categoryId, minPrice, maxPrice, pageable)
                .map(ProductMapper::toResponse);
    }


}
