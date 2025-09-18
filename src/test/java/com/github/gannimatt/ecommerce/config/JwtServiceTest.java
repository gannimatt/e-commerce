package com.github.gannimatt.ecommerce.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtServiceTest {

    @Test
    void generateAndParse_ShouldReturnSameSubject() {
        String secret = "this-is-a-long-test-secret-string-at-least-32-chars";
        long expirationMs = 3_600_000L;
        JwtService jwt = new JwtService(secret, expirationMs);

        String subject = "user@example.com";
        String token = jwt.generate(subject, Map.of("roles", "ROLE_ADMIN"));

        Jws<Claims> parsed = jwt.parse(token);
        assertNotNull(parsed);
        assertEquals(subject, parsed.getBody().getSubject());
    }

}


