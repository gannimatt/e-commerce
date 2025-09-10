package com.github.gannimatt.ecommerce.service;

import com.github.gannimatt.ecommerce.entity.Product;
import com.github.gannimatt.ecommerce.dto.*;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAll();
    ProductResponse getById(Long id);
    ProductResponse create(ProductRequest req);
    ProductResponse update(Long id, ProductRequest req);
    void delete(Long id);
}
