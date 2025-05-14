package com.devtale.ecommerceplatform2.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private BigDecimal price;

    private int inventory;
    private String description;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="category_id")
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Image> images = new ArrayList<>();
}
