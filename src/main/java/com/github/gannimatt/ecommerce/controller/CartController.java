package com.github.gannimatt.ecommerce.controller;

import com.github.gannimatt.ecommerce.dto.CartItemRequest;
import com.github.gannimatt.ecommerce.dto.CartResponse;
import com.github.gannimatt.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/cart") @Tag(name = "Cart")
public class CartController {
    private final CartService service;
    public CartController(CartService service) { this.service = service; }

    private String email() { return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping public CartResponse get() { return service.getCart(email()); }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/items") public CartResponse add(@Valid @RequestBody CartItemRequest req) {
        return service.addItem(email(), req);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @DeleteMapping("/items/{productId}") public CartResponse remove(@PathVariable Long productId) {
        return service.removeItem(email(), productId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @DeleteMapping public void clear() { service.clear(email()); }
}