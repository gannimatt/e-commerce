//package com.github.gannimatt.ecommerce.service;
//
//import com.github.gannimatt.ecommerce.dto.ProductRequest;
//import com.github.gannimatt.ecommerce.dto.ProductResponse;
//import com.github.gannimatt.ecommerce.entity.Product;
//import com.github.gannimatt.ecommerce.repository.ProductRepository;
//import com.github.gannimatt.ecommerce.service.impl.ProductServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProductServiceTest {
//
//    @Mock private ProductRepository repo;
//    @InjectMocks private ProductServiceImpl service;
//
//    @Test
//    void create_shouldSaveAndReturnResponse() {
//        var req = new ProductRequest("P-001", "Mouse", "Wireless mouse", BigDecimal.valueOf(19.99));
//        var saved = new Product(1L, "P-001", "Mouse", "Wireless mouse", BigDecimal.valueOf(19.99));
//
//        when(repo.findBySku("P-001")).thenReturn(Optional.empty()); // enforce SKU uniqueness
//        when(repo.save(any(Product.class))).thenReturn(saved);
//
//        ProductResponse resp = service.create(req);
//
//        assertThat(resp).isNotNull();
//        assertThat(resp.id()).isEqualTo(1L);
//        assertThat(resp.sku()).isEqualTo("P-001");
//        verify(repo).save(any(Product.class));
//    }
//
//    @Test
//    void getById_shouldReturnResponse() {
//        var product = new Product(1L, "P-001", "Mouse", "Wireless mouse", BigDecimal.valueOf(19.99));
//        when(repo.findById(1L)).thenReturn(Optional.of(product));
//
//        ProductResponse resp = service.getById(1L);
//
//        assertThat(resp.name()).isEqualTo("Mouse");
//        verify(repo).findById(1L);
//    }
//
//    @Test
//    void getAll_shouldReturnAllProducts() {
//        var product = new Product(1L, "P-001", "Mouse", "Wireless mouse", BigDecimal.valueOf(19.99));
//        when(repo.findAll()).thenReturn(List.of(product));
//
//        List<ProductResponse> responses = service.getAll();
//
//        assertThat(responses).hasSize(1);
//        assertThat(responses.get(0).sku()).isEqualTo("P-001");
//    }
//
//    @Test
//    void search_blankQuery_shouldUseFindAll() {
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
//        var product = new Product(1L, "P-001", "Mouse", "Wireless mouse", BigDecimal.valueOf(19.99));
//        when(repo.findAll(pageable)).thenReturn(new PageImpl<>(List.of(product)));
//
//        var page = service.search("", pageable);
//
//        assertThat(page.getTotalElements()).isEqualTo(1);
//        assertThat(page.getContent().get(0).name()).isEqualTo("Mouse");
//    }
//
//    @Test
//    void search_withQuery_shouldUseSearchMethod() {
//        Pageable pageable = PageRequest.of(0, 10);
//        when(repo.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase("mouse", "mouse", pageable))
//                .thenReturn(Page.empty());
//
//        var page = service.search("mouse", pageable);
//
//        verify(repo).findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase("mouse", "mouse", pageable);
//        assertThat(page).isNotNull();
//    }
//}
