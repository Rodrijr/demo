package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000") 
public class AuthController {

    private final AuthService authService;
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Este es un mensaje de información" + loginRequest);
        
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String token = authService.login(email, password);
        
        if (token != null) {
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response); // Devuelve el mapa como JSON
        } else {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Credenciales incorrectas");
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
    public static class LoginRequest {
        private String email;
        private String password;
        private String username;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        public String getUsername() {
            return username;
        }

        public void setUserName(String username) {
            this.username = username;
        }


        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
