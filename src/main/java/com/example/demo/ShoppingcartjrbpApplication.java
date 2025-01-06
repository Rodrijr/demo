package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@SpringBootApplication
public class ShoppingcartjrbpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingcartjrbpApplication.class, args);
    }

   @Bean
    public CommandLineRunner loadData(ProductRepository repository) {
        return args -> {
            //repository.save(new Product("Cellphone", 10.5, "Xiaomi k50 pro", "50"));
           // repository.save(new Product("Jane Smith", "jane.smith@example.com"));
        };
    }
}
