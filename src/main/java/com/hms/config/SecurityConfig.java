package com.hms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;

import java.security.Security;

@Configuration
public class SecurityConfig {


    private JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            @org.jetbrains.annotations.NotNull HttpSecurity http
    ) throws Exception{

        //h(cd)2
        http.csrf().disable().cors().disable();
        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);

        //haap
        //http.authorizeHttpRequests().anyRequest().permitAll();
        http.authorizeHttpRequests().requestMatchers("/api/v1/users/login", "/api/v1/users/signup" )
                .permitAll()
                .anyRequest().authenticated();

         return http.build();
    }
}
