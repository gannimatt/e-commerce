package com.github.gannimatt.ecommerce.service;

import com.github.gannimatt.ecommerce.entity.Product;
import com.github.gannimatt.ecommerce.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAll();
    ProductResponse getById(Long id);
    ProductResponse create(ProductRequest req);
    ProductResponse update(Long id, ProductRequest req);
    void delete(Long id);
    Page<ProductResponse> search(String q, Pageable pageable, Long categoryId);

}
