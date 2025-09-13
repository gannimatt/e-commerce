// OrderRepository.java
package com.github.gannimatt.ecommerce.repository;
import com.github.gannimatt.ecommerce.entity.Order;
import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserEmailOrderByCreatedAtDesc(String email, Pageable pageable);
}
