package com.github.gannimatt.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        BigDecimal total,
        List<CartLine> lines
) {
    public record CartLine(
            Long productId,
            String sku,
            String name,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal
    ) {}
}
