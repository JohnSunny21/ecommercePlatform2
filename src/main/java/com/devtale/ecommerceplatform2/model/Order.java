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
@Table(name = "orders")
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

    @PrePersist
    private void setOrderDate() {
        if (orderDate == null) {
            orderDate = LocalDate.now();
        }
    }

    public void addOrderItem(OrderItem item) {
        if (item != null) {
            orderItems.add(item);
            item.setOrder(this);
            updateTotalAmount();
        }
    }

    public void removeOrderItem(OrderItem item){
        if(item != null && orderItems.contains(item)){
            orderItems.remove(item);
            item.setOrder(null);
            updateTotalAmount();
        }
    }

    private void updateTotalAmount(){
        this.totalAmount = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

}
