package com.api.gateway.constant;

import java.util.List;
import java.util.Set;

public class GatewayConstant {

    public static final Set<String> allowedPath= Set.of("/auth-service/api/v1/login","/auth-service/actuator/");
    public static String errorResponse = """
                        {
                          "error": "Error Occurred",
                          "message": "Failed to connect",
                          "details": "%s"
                        }
                        """;
}
