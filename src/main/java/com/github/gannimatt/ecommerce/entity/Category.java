package com.github.gannimatt.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories", uniqueConstraints = {
        @UniqueConstraint(name = "uk_category_slug", columnNames = "slug")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)            private String name;
    @Column(nullable = false, length=80) private String slug;
    private String description;
}
