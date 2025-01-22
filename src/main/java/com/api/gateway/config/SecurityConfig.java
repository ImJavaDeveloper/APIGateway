package com.api.gateway.config;

import com.api.gateway.constant.GatewayConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange->
                        exchange.pathMatchers("/auth-service/**","/user-service/**","/bookmark-service/**").permitAll()
                                .anyExchange().authenticated())
                .build();
    }

    /*
     CORS Config
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        return new CorsWebFilter(corsConfigurationSource());
    }

    private UrlBasedCorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(GatewayConstant.allowedHeaders);
        corsConfiguration.setExposedHeaders(GatewayConstant.allowedExposedHeaders);
        corsConfiguration.setAllowedMethods(GatewayConstant.allowedMethods);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Apply this to all routes
        return source;
    }
}
