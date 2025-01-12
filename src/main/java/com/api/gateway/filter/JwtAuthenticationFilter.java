package com.api.gateway.filter;

import com.api.gateway.constant.GatewayConstant;
import com.api.gateway.exception.DownstreamServiceException;
import com.api.gateway.exception.UnAuthorizedException;
import com.api.gateway.feign.AuthServiceClient;
import com.api.gateway.feign.TokenResponse;
import feign.FeignException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    @Value("${app.token.secret}")
    private String jwtSecretKey;
    @Autowired
    AuthServiceClient authServiceClient;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader=exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info(exchange.getRequest().getPath().value());
        if(GatewayConstant.allowedPath.contains(exchange.getRequest().getPath().value())
        || exchange.getRequest().getPath().value().startsWith("/auth-service/actuator/"))
        {
            return chain.filter(exchange);
        }
        if(authHeader == null || !authHeader.startsWith("Bearer"))
        {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        try{
            String token=authHeader.substring(7);
            TokenResponse tokenResponse;
            try {
                 tokenResponse= authServiceClient.validateToken(authHeader);
            } catch (FeignException.NotFound ex) {
                log.info("FeignException Occured...");
                throw new UnAuthorizedException("Resource not found during Feign call");
            } catch (FeignException ex) {
                throw new Exception("Error occurred during Feign call");
            }
            if(tokenResponse.isAuthenticated() && tokenResponse.getUsername() != null) {
                exchange.getRequest().mutate().header("REMOTE_USER", tokenResponse.getUsername());
            }
            else {
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }catch (Exception e)
        {
            log.error("Error While Validating Token:{}",e);
            exchange.getResponse()
                    .setStatusCode(HttpStatus.NOT_FOUND)

            ;
            throw new UnAuthorizedException("Resource not found during Feign call");
           // return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
}
