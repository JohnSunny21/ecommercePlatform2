package com.devtale.ecommerceplatform2.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder // Enables fluent object creation
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ensures totalAmount is initialized properly
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    @ToString.Exclude // Prevents potential infinite recursion in toString()
    /**
     * We used the ToString Exclude because this field has the bidirectional relationships, calling
     * toString() can lead to infinite recursion. causing the StackOverflowError.
     *
     * What Happens When You Call toString()?
     * - If you print a Cart object, Lombok's toString() method will include items.
     * - Each CartItem also has a reference to Cart, so its toString() will include the Cart object.
     * - This creates an infinite loop.
     *
     * Cart -> CartItem -> Cart -> CartItem -> Cart -> ... ===> Leads to StackOverflowError.
     * By using the ToString().EXCLUDE Now, when calling toString() on a Cart object, it won't include items, preventing infinite recursion.
     */
    private Set<CartItem> items = new HashSet<>();


    /**
     * - @JoinColumn(name = "user_id") tells Hibernate to create a foreign key column (user_id) in the Cart table.
     * - The foreign key references the id column in the User table.
     * - Spring Boot finds the primary key (id) in User automatically because User is annotated with @Id.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    /**
     * Checks whether the item is null and adds the item to the cart and updates the total amount.
     * Ensures bidirectional relationship consistency.
     *
     */
    public void addItem(CartItem item){
        if(item != null){
            this.items.add(item);
            item.setCart(this);
            updateTotalAmount();
        }
    }

    /**
     *Checks the item for the null && also check whether it exists in the cart if exists then
     *  Removes an item from the cart and updates the total amount.
     *  Ensures bidirectional relationship consistency.
     *
     */
    public void removeItem(CartItem item){
        if (item != null && this.items.contains(item)){
            this.items.remove(item);
            item.setCart(null);
            updateTotalAmount();
        }
    }

    /**
    Clears all items from the cart and resets the total amount.
     */
    public void clearCart(){
        this.items.clear();
        updateTotalAmount();
    }

    /**
     * Updates the total amount based on the items in the cart.
     * Ensures null safety and avoids potential erros.
     */
    private void updateTotalAmount(){
        this.totalAmount = items.stream()
                .map(item -> {
                    BigDecimal unitPrice = item.getUnitPrice();
                    int quantity = item.getQuantity();
                    return (unitPrice != null) ? unitPrice.multiply(BigDecimal.valueOf(quantity)) : BigDecimal.ZERO;

                })
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }


    /**
     * Retuns an unmodifiable view of the items to prevent unintended modifications.
     * This ensures better encapsulation and thread safety.
     */
    public Set<CartItem> getItems(){
        return Collections.unmodifiableSet(items);
    }

}
