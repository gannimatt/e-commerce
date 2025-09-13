package com.github.gannimatt.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;

import java.util.ArrayList; import java.util.List;

@Entity @Table(name = "carts", uniqueConstraints = @UniqueConstraint(name="uk_cart_email", columnNames = "userEmail"))
@Getter @Setter
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String userEmail; // from JWT

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public void clear() { items.clear(); }
}
