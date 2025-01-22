package com.api.gateway.filter;

import com.api.gateway.constant.GatewayConstant;
import com.api.gateway.exception.DownstreamServiceException;
import com.api.gateway.exception.UnAuthorizedException;
import com.api.gateway.feign.AuthServiceClient;
import com.api.gateway.feign.TokenResponse;
import com.api.gateway.utils.JWTUtils;
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
    @Autowired
    JWTUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        log.info(exchange.getRequest().getPath().value());
        log.info(exchange.getRequest().getMethod().toString());
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequest().getMethod().toString())) {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }

        log.info("Whitelisted URLS:{}",GatewayConstant.allowedPath);
        if(GatewayConstant.allowedPath.contains(exchange.getRequest().getPath().value())
                || exchange.getRequest().getPath().value().contains("/actuator/")
                || exchange.getRequest().getPath().value().contains("swagger")
                || exchange.getRequest().getPath().value().contains("/v3/api-docs"))
        {log.info("Whitelisted URLS:{}",GatewayConstant.allowedPath);
            return chain.filter(exchange);
        }

        String authHeader=exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer"))
        {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);
            String errorResponse = """
                        {
                          "error": "Invalid Token !!",
                          "message": "Bearer Token Is Missing"
                        }
                        """;
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(errorResponse.getBytes())));

        }
        try{
           //TokenResponse tokenResponse=authServiceClient.validateToken(authHeader);
            String token=authHeader.substring(7);
           // String token=jwtUtils.parseJwt(authHeader);
            boolean isValid=jwtUtils.validateJwtToken(token);
            log.info("Token Response:{}", token);
            if(!isValid)
            {
                throw new RuntimeException("UnAuthorized !!");
            }
            Claims claims=Jwts.parser().setSigningKey(GatewayConstant.JWTSECRET).parseClaimsJws(token).getBody();
            ServerWebExchange exchange1= exchange.mutate().request(request->request.header("REMOTE_USER",claims.getSubject())).build();
            return chain.filter(exchange1);

        }catch (Exception e)
        {
            log.error("Error While Validating JWT Token");
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);
            String errorResponse=GatewayConstant.errorResponse.formatted(e.getMessage());
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(errorResponse.getBytes())));

        }
        ///return chain.filter(exchange);
    }
}
