package com.devtale.ecommerceplatform2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();


    public Category(String name){
        this.name = name;
    }
}
