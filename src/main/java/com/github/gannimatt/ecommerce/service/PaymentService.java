package com.github.gannimatt.ecommerce.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    /**
     * Mock payment processor.
     * Always returns true to simulate success.
     * Could be extended later to randomly fail or integrate a real gateway.
     */
    public boolean processPayment(String userEmail, BigDecimal amount) {
        // For demo: just succeed
        return true;
    }
}
