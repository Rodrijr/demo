package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public PurchaseOrder createOrderFromCart(Long userId, String address) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado para el usuario ID: " + userId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para el ID: " + userId));

        PurchaseOrder order = new PurchaseOrder();
        order.setUserId(userId);
        order.setCustomerName(user.getName());
        order.setDeliveryAddress(address != null ? address : user.getAddress());
        order.setStatus("send pending");

        double totalAmount = 0.0;
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();

            orderItem.setPurchaseOrder(order);
            order.addItem(orderItem);
            totalAmount += cartItem.getPrice() * cartItem.getQuantity();
        }
        order.setTotalAmount(totalAmount);

        cart.getItems().clear();
        cartRepository.save(cart);

        return purchaseOrderRepository.save(order);
    }
}
