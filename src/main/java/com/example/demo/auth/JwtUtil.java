package com.example.demo.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 86400000; // 24 horas

    // Generar el token JWT utilizando el email
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)  // El 'email' es el subject del token
                .setIssuedAt(new Date())  // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Expiración del token
                .signWith(SECRET_KEY)  // Firmar el token con la clave secreta
                .compact();
    }

    // Validar el token JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
            return true;  // El token es válido
        } catch (Exception e) {
            return false;  // El token no es válido
        }
    }

    // Obtener el email del token JWT
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Extraer el 'subject' (en este caso el email)
    }
}
