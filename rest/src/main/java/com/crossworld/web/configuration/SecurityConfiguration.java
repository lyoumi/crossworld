package com.crossworld.web.configuration;

import com.crossworld.web.security.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            AuthFilter cwdAuthFilter,
            ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .addFilterAt(cwdAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange().anyExchange().permitAll()
                .and()
                .build();
    }
}
