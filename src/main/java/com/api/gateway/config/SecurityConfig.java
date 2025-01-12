package com.api.gateway.config;

import com.api.gateway.exception.GatewayAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private GatewayAuthenticationEntryPoint authenticationEntryPoint;
    public SecurityConfig(GatewayAuthenticationEntryPoint authenticationEntryPoint)
    {
      this.authenticationEntryPoint=authenticationEntryPoint;
    }
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange->exchange
                        .pathMatchers("/auth-service/api/v1/login","/auth-service/**").permitAll()
                       // .anyExchange().authenticated()
                )
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec.authenticationEntryPoint(authenticationEntryPoint))
                .build();
    }
}
