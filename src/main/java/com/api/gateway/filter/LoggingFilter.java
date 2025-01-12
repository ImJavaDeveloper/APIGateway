package com.api.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("Request:{},{}",exchange.getRequest().getMethod(),exchange.getRequest().getPath());
        return chain.filter(exchange).doOnTerminate(
                ()->{log.info("Response:{}",exchange.getResponse().getStatusCode());}
        );
    }
}
