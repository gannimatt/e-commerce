package com.github.gannimatt.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories", uniqueConstraints = {
        @UniqueConstraint(name = "uk_category_slug", columnNames = "slug"),
        @UniqueConstraint(name = "uk_category_name", columnNames = "name")
})
@Getter @Setter
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String name;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String slug;

    @Size(max = 500)
    @Column(length = 500)
    private String description;
}
