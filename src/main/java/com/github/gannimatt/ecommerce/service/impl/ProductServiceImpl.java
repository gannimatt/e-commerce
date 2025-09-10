package com.github.gannimatt.ecommerce.service.impl;

import com.github.gannimatt.ecommerce.entity.Product;
import com.github.gannimatt.ecommerce.exception.NotFoundException;
import com.github.gannimatt.ecommerce.repository.ProductRepository;
import com.github.gannimatt.ecommerce.service.ProductService;
import com.github.gannimatt.ecommerce.mapper.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.github.gannimatt.ecommerce.dto.*;
import static com.github.gannimatt.ecommerce.mapper.ProductMapper.*;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override public List<ProductResponse> getAll() {
        return repo.findAll().stream().map(ProductMapper::toResponse).toList();
    }

    @Override public ProductResponse getById(Long id) {
        var p = repo.findById(id).orElseThrow(() -> new NotFoundException("Product " + id + " not found"));
        return toResponse(p);
    }

    @Override public ProductResponse create(ProductRequest req) {
        repo.findBySku(req.sku()).ifPresent(p -> { throw new IllegalArgumentException("SKU already exists"); });
        var saved = repo.save(toEntity(req));
        return toResponse(saved);
    }

    @Override public ProductResponse update(Long id, ProductRequest req) {
        var p = repo.findById(id).orElseThrow(() -> new NotFoundException("Product " + id + " not found"));
        // if sku is changing, ensure uniqueness
        if (!p.getSku().equals(req.sku()) && repo.findBySku(req.sku()).isPresent()) {
            throw new IllegalArgumentException("SKU already exists");
        }
        p.setSku(req.sku()); p.setName(req.name()); p.setDescription(req.description()); p.setPrice(req.price());
        return toResponse(repo.save(p));
    }

    @Override public void delete(Long id) {
        if (!repo.existsById(id)) throw new NotFoundException("Product " + id + " not found");
        repo.deleteById(id);
    }

    @Override
    public Page<ProductResponse> search(String q, Pageable pageable) {
        Page<Product> page = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(q, q, pageable);
        return page.map(ProductMapper::toResponse);
    }

}
