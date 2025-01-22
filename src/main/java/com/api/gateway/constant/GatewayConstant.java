package com.api.gateway.constant;

import java.util.List;
import java.util.Set;

public class GatewayConstant {

    public static final String JWTSECRET="secretKeyRahatAliCognizantSecretKeyForAuthServices808756";
    public static final Set<String> allowedPath= Set.of(
            "/auth-service/api/v1/login",
            "/auth-service/api/v1/validate",
            "/user-service/api/v1/register",
            "/auth-service/swagger-ui/index.html");
    public static String errorResponse = """
                        {
                          "error": "Error Occurred !!",
                          "message": "Failed to connect",
                          "details": "%s"
                        }
                        """;

    public final static List<String> allowedOrigins= List.of(
            "http://localhost:4200"
    );

    public final static List<String> allowedHeaders=List.of(
            "Origin",
            "Access-Control-Allow-Origin",
            "Content-Type",
            "Accept", "Authorization",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "Access-Control-Allow-Headers",
            "Access-Control-Allow-Credentials"
    );

    public final static List<String> allowedMethods=List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
    );

    public final static List<String> allowedExposedHeaders=List.of(
            "Origin",
            "Content-Type",
            "Accept",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Headers",
            "Access-Control-Allow-Credentials"
    );
}
