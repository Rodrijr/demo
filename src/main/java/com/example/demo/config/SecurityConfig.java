package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.auth.JwtFilter;
import com.example.demo.auth.JwtUtil;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() 
            .authorizeRequests()
                .requestMatchers("/auth/login").permitAll()  
                .requestMatchers(HttpMethod.POST, "/users").permitAll()  
                .requestMatchers(HttpMethod.GET, "/users").authenticated() 
                .requestMatchers(HttpMethod.GET, "/products").permitAll()
                .requestMatchers(HttpMethod.POST, "/cart/add/{cartId}").authenticated() 
                .requestMatchers(HttpMethod.GET, "/cart/{userId}").authenticated() 
                .requestMatchers(HttpMethod.POST, "/cart").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/cart/clean/{cartId}").authenticated()
                .requestMatchers(HttpMethod.PUT, "/cart/{userId}").authenticated() 
                .requestMatchers(HttpMethod.POST, "/orders/{userId}").authenticated()
                .requestMatchers(HttpMethod.GET, "/orders/{userId}").authenticated()
                .anyRequest().authenticated() 
            .and()
            .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class) 
            .cors(); 

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
