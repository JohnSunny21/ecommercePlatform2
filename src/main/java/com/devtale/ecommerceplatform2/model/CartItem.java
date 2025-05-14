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

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE) // Prevents unintended deletion of Cart
    @JoinColumn(name = "cart_id")
    @ToString.Exclude // Prevents the infinite recursion
    private Cart cart;

    public void setTotalPrice(){
        if(unitPrice != null){
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }else{
            this.totalPrice = BigDecimal.ZERO;
        }
    }

    @PrePersist
    @PreUpdate
    private void updateTotalPrice(){
        setTotalPrice();
    }
}
