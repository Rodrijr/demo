package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.OrderItem;
import com.example.demo.model.PurchaseOrder;
import com.example.demo.model.User;
import com.example.demo.repository.PurchaseOrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PurchaseOrderService;


@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService orderService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    public PurchaseOrderController(PurchaseOrderService orderService, UserRepository userRepository,  PurchaseOrderRepository purchaseOrderRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @PostMapping("/{userId}")
    public PurchaseOrder createOrderWithPayload(@PathVariable Long userId, @RequestBody Map<String, Object> orderPayload) {
  
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para el ID: " + userId));

        PurchaseOrder order = new PurchaseOrder();
        order.setUserId(userId); 
        order.setCustomerName(user.getName());

        String address = (String) orderPayload.get("address");
        order.setDeliveryAddress(address != null ? address : user.getAddress()); 

        order.setStatus("pending");

        double totalAmount = 0.0;
        Object itemsObject = orderPayload.get("items");

        if (itemsObject instanceof List) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) itemsObject;

            for (Map<String, Object> item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(((Number) item.get("id")).longValue());  
                orderItem.setProductName((String) item.get("name"));
                orderItem.setQuantity((Integer) item.get("quantity")); 
                orderItem.setPrice(((Number) item.get("price")).doubleValue());

                orderItem.setDescription((String) item.get("description"));  
                orderItem.setImage((String) item.get("image")); 
                order.addItem(orderItem);

                totalAmount += orderItem.getPrice() * orderItem.getQuantity();
            }
        } else {
            throw new IllegalArgumentException("El formato de los items no es válido.");
        }

        order.setTotalAmount(totalAmount);
        return purchaseOrderRepository.save(order);
    }

    @GetMapping("/{userId}")
    public List<PurchaseOrder> getOrdersByUserId(@PathVariable Long userId) {
        List<PurchaseOrder> orders = purchaseOrderRepository.findByUserId(userId);
        
        if (orders.isEmpty()) {
            throw new RuntimeException("No se encontraron órdenes de compra para el usuario con ID: " + userId);
        }

        return orders;
    }
    
}
