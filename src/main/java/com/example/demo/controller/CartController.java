package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartRepository;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/{userId}")
    public Cart getCartByUserId(@PathVariable Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));
    }

    @PostMapping("/add/{cartId}")
    public Cart addProductToCart(@PathVariable Long cartId, @RequestBody CartItem cartItem) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found for ID: " + cartId));
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItem.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem itemToUpdate = existingItem.get();
            itemToUpdate.setQuantity(itemToUpdate.getQuantity() + cartItem.getQuantity());
        } else {
            cart.getItems().add(cartItem);
        }

        return cartRepository.save(cart);
    }
    @DeleteMapping("/clean/{cartId}")
    public Cart cleanCart(@PathVariable Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found for ID: " + cartId));
        
        cart.getItems().clear();

        return cartRepository.save(cart);
    }
}
