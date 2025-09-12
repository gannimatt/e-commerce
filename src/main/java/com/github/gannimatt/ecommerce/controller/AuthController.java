package com.github.gannimatt.ecommerce.controller;

import com.github.gannimatt.ecommerce.dto.AuthResponse;
import com.github.gannimatt.ecommerce.dto.LoginRequest;
import com.github.gannimatt.ecommerce.dto.RegisterRequest;
import com.github.gannimatt.ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        return service.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return service.login(req);
    }
}
