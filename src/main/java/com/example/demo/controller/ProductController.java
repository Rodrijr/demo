package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductRepository ProductRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return ProductRepository.findAll();
    }

    @PostMapping
    public Product createProduct(@RequestBody Product Product) {
    	 System.out.println("Product: " + Product);
        return ProductRepository.save(Product);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return ProductRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
