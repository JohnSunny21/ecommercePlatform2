package com.devtale.ecommerceplatform2.service.cart;

import com.devtale.ecommerceplatform2.exceptions.ResourceNotFoundException;
import com.devtale.ecommerceplatform2.model.Cart;
import com.devtale.ecommerceplatform2.model.CartItem;
import com.devtale.ecommerceplatform2.model.Product;
import com.devtale.ecommerceplatform2.repository.CartItemRepository;
import com.devtale.ecommerceplatform2.repository.CartRepository;
import com.devtale.ecommerceplatform2.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;


    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        /*
        In order to add the Item to the cart need to do some things first.
        Get the Cart.
        Get the Product
        Check if the product already in the cart.
        If yes, then increase the quantity with the requested quantity.
        If no, then initiate a new CartItem entry.

         */
        Cart cart  = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if(cartItem.getId() == null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId,productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item -> item.getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                    });
        BigDecimal totalAmount = cart.getItems()
                .stream().map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);

    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }
}
