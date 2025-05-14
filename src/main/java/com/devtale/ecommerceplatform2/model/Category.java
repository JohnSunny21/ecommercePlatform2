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
@Builder // Enables fluent object creation
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore // Prevents infinite recursion in JSON serialization
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
    @ToString.Exclude // Prevents infinite recursion in toString()
    //private List<Product> products = new ArrayList<>(); // Ensures proper initialization. Avoids the NullPointerException.
    private List<Product> products;

    /**
     * Constructor for creating a Category with a name.
     * Uses @AllArgsConstructor instead for flexibility.
     * @param name
     */
    public Category(String name){
        this.name = name;
    }
}
