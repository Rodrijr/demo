package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.auth.JwtUtil;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import java.util.logging.Logger;
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());
    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {
        // Buscar el usuario por username
        User user = userRepository.findByEmail(email) ;
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Este es un mensaje de información" + user.getEmail());
        // Verificar si el usuario existe y si la contraseña es correcta
        if (user != null && user.getPassword().equals(password)) {
            return jwtUtil.generateToken(email); // Generar el JWT
        }
        return null;
    }
}
