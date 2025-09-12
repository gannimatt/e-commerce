package com.github.gannimatt.ecommerce.service;

import com.github.gannimatt.ecommerce.dto.AuthResponse;
import com.github.gannimatt.ecommerce.dto.LoginRequest;
import com.github.gannimatt.ecommerce.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest req);
    AuthResponse login(LoginRequest req);
}
