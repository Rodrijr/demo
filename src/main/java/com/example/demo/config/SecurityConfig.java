package com.example.demo.config;

import com.example.demo.auth.JwtFilter;
import com.example.demo.auth.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Deshabilitar CSRF (si es necesario para el flujo JWT)
            .authorizeRequests()
                .requestMatchers("/auth/login").permitAll()  // Permitir acceso sin autenticación a /auth/login
                .requestMatchers("/users").authenticated()
                .anyRequest().authenticated()  // Requiere autenticación para todas las demás rutas
            .and()
            .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);  // Añadir el filtro JWT antes del filtro de autenticación

        return http.build();
    }

    // Bean para el AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
