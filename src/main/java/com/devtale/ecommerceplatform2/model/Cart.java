package com.devtale.ecommerceplatform2.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    @ToString.Exclude // Prevents potential infinite recursion in toString()
    private Set<CartItem> items = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public void addItem(CartItem item){
        if(item != null){
            this.items.add(item);
            item.setCart(this);
            updateTotalAmount();
        }
    }

    public void removeItem(CartItem item){
        if (item != null && this.items.contains(item)){
            this.items.remove(item);
            item.setCart(null);
            updateTotalAmount();
        }
    }

    public void clearCart(){
        this.items.clear();
        updateTotalAmount();
    }

    private void updateTotalAmount(){
        this.totalAmount = items.stream()
                .map(item -> {
                    BigDecimal unitPrice = item.getUnitPrice();
                    int quantity = item.getQuantity();
                    return (unitPrice != null) ? unitPrice.multiply(BigDecimal.valueOf(quantity)) : BigDecimal.ZERO;

                })
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }


    public Set<CartItem> getItems(){
        return Collections.unmodifiableSet(items);
    }

}
