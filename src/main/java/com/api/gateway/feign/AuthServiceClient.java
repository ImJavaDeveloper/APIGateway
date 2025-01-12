package com.api.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "AUTHSERVICE", url = "http://localhost:8080",configuration = FeignConfig.class)
public interface AuthServiceClient {

    @GetMapping("/api/v1/validate")
    TokenResponse validateToken(@RequestHeader("Authorization") String authorizationHeader);
}
