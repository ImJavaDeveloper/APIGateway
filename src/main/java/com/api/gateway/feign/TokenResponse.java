package com.api.gateway.feign;

import lombok.Data;

@Data
public class TokenResponse {

    private String token;
    private boolean isAuthenticated;
    private String username;
}
