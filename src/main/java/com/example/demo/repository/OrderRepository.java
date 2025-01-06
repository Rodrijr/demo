package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.example.demo.model.PurchaseOrder;

@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder, Long> {
    // Puedes agregar métodos personalizados aquí si es necesario
    //List<PurchaseOrder> findByUserId(Long userId);
}