package com.github.gannimatt.ecommerce.controller;

import com.github.gannimatt.ecommerce.config.JwtAuthFilter;
import com.github.gannimatt.ecommerce.dto.ProductResponse;
import com.github.gannimatt.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerWebTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter; // prevent creation of real security filter bean

    @Test
    void publicGetById_shouldBe200() throws Exception {
        when(productService.getById(1L))
                .thenReturn(new ProductResponse(1L, "SKU", "Name", "Desc", new BigDecimal("1.00"), null, null));

        mvc.perform(get("/api/products/1"))
                .andExpect(status().isOk());
    }
}


