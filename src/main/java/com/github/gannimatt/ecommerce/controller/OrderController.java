package com.github.gannimatt.ecommerce.controller;

import com.github.gannimatt.ecommerce.dto.OrderResponse;
import com.github.gannimatt.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/orders") @Tag(name = "Orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service) { this.service = service; }

    private String email() { return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/checkout") public OrderResponse checkout() { return service.checkout(email()); }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping public Page<OrderResponse> myOrders(Pageable pageable) { return service.listMyOrders(email(), pageable); }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/{id}") public OrderResponse get(@PathVariable Long id) { return service.getMyOrder(email(), id); }
}