package com.github.gannimatt.ecommerce.controller;

import com.github.gannimatt.ecommerce.service.ProductService;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.github.gannimatt.ecommerce.dto.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductResponse create(@RequestBody @Valid ProductRequest req) {
        return service.create(req);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @RequestBody @Valid ProductRequest req) {
        return service.update(id, req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping
    public Page<ProductResponse> search(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(required = false) String q,
                                      @RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) BigDecimal minPrice,
                                      @RequestParam(required = false) BigDecimal maxPrice,
                                      Pageable pageable) {
        Pageable effectivePageable = PageRequest.of(page, size, pageable.getSort());
        return service.search(q, effectivePageable, categoryId, minPrice, maxPrice);
    }
}