package com.devtale.ecommerceplatform2.model;

import com.devtale.ecommerceplatform2.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders") // Avoids conflicts with SQL keywords
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private LocalDate orderDate;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<OrderItem> orderItems = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    /**
     * Automatically sets the order date before persisting to the database.
     */
    @PrePersist
    private void setOrderDate() {
        if (orderDate == null) {
            orderDate = LocalDate.now();
        }
    }

    /**
     * Check if the item is null and add the item to the orderItems.
     * Ensures bidirectional consistency.
     * @param item
     */
    public void addOrderItem(OrderItem item) {
        if (item != null) {
            orderItems.add(item);
            item.setOrder(this);
            updateTotalAmount();
        }
    }

    /**
     * Check it item is null and also checks if the items contains the item.
     * Then remove the item from the orderItems.
     * Ensures bidirectional consistency.
     * @param item
     */
    public void removeOrderItem(OrderItem item){
        if(item != null && orderItems.contains(item)){
            orderItems.remove(item);
            item.setOrder(null);
            updateTotalAmount();
        }
    }

    /**
     * Updated the total amount based on the ordered items.
     */
    private void updateTotalAmount(){
        this.totalAmount = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

}
