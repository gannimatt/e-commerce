package com.github.gannimatt.ecommerce;

import com.github.gannimatt.ecommerce.entity.Product;
import com.github.gannimatt.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductPagingIT {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Autowired
    ProductRepository repo;

    @BeforeEach
    void seed() {
        repo.deleteAll();
        repo.save(Product.builder().sku("M-001").name("Mouse A").price(new BigDecimal("15.00")).build());
        repo.save(Product.builder().sku("M-002").name("Mouse B").price(new BigDecimal("25.00")).build());
        repo.save(Product.builder().sku("K-001").name("Keyboard A").price(new BigDecimal("35.00")).build());
    }

    @Test
    void listPagedAndSearchByQuery() {
        String url = "http://localhost:" + port + "/api/products?q=mouse&page=0&size=1&sort=price,desc";

        ResponseEntity<Map<String,Object>> response = rest.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String,Object> body = response.getBody();
        assertThat(body).isNotNull();

        // content is a list of ProductResponse maps
        List<?> content = (List<?>) body.get("content");
        assertThat(content).hasSize(1);

        @SuppressWarnings("unchecked")
        Map<String,Object> first = (Map<String,Object>) content.get(0);
        assertThat(first.get("sku")).isEqualTo("M-002"); // highest price mouse

        assertThat((Number) body.get("totalElements")).hasToString("2");
    }
}
