package com.github.gannimatt.ecommerce.service.impl;

import com.github.gannimatt.ecommerce.config.JwtService;
import com.github.gannimatt.ecommerce.dto.AuthResponse;
import com.github.gannimatt.ecommerce.dto.LoginRequest;
import com.github.gannimatt.ecommerce.dto.RegisterRequest;
import com.github.gannimatt.ecommerce.entity.Role;
import com.github.gannimatt.ecommerce.entity.User;
import com.github.gannimatt.ecommerce.repository.UserRepository;
import com.github.gannimatt.ecommerce.service.AuthService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthServiceImpl(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @Override
    public AuthResponse register(RegisterRequest req) {
        if (users.existsByEmailIgnoreCase(req.email())) {
            throw new DataIntegrityViolationException("Email already in use");
        }
        User u = new User();
        u.setEmail(req.email().toLowerCase());
        u.setPassword(encoder.encode(req.password()));
        u.setRoles(Set.of(Role.ROLE_CUSTOMER));
        users.save(u);
        String token = jwt.generate(u.getEmail(), Map.of("roles", u.getRoles()));
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        User u = users.findByEmail(req.email().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!encoder.matches(req.password(), u.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwt.generate(u.getEmail(), Map.of("roles", u.getRoles()));
        return new AuthResponse(token);
    }
}
