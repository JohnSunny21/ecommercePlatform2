package com.devtale.ecommerceplatform2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder  // Enables fluent object creation.
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * - Prevents infinite recursion when serializing CartItem to JSON.
     * - Avoids unnecessary data exposure (e.g., when returning API responses).
     * - Improves performance by reducing JSON payload size.
     * Example Without @JsonIgnore (Causing Infinite Recursion)
     * If Cart contains a Set<CartItem> and CartItem contains a reference to Cart, serializing CartItem will cause circular references:
     * {
     *   "id": 1,
     *   "quantity": 2,
     *   "unitPrice": 10.99,
     *   "cart": {
     *     "id": 5,
     *     "items": [
     *       {
     *         "id": 1,
     *         "quantity": 2,
     *         "unitPrice": 10.99,
     *         "cart": { ... } // Infinite loop!
     *       }
     *     ]
     *   }
     * }
     */
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE) // Prevents unintended deletion of Cart
    @JoinColumn(name = "cart_id")
    @ToString.Exclude // Prevents the infinite recursion
    private Cart cart;


    /**
     * Calculates and sets the total price based on unit price and quantity.
     * Ensures null safety to prevent errors.
     */
    public void setTotalPrice(){
        if(unitPrice != null){
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }else{
            this.totalPrice = BigDecimal.ZERO;
        }
    }

    /**
     * Automatically updates total price before presisting to the database.
     */
    @PrePersist
    @PreUpdate
    private void updateTotalPrice(){
        setTotalPrice();
    }
}
