package com.api.gateway.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;

//Spring Cloud OpenFeign provides the ErrorDecoder interface to customize error responses.

@Slf4j
public class GatewayErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        log.info("message:{} and status:{}",s,response.status());
        switch (response.status()) {
            case 400:
                return new BadRequestException("Bad Request from downstream service");
            case 404:
                return new ResourceNotFoundException("Resource not found");
            case 401:
                return new UnAuthorizedException("Not Authorized...");
            case 500:
                return new DownstreamServiceException("Internal server error in downstream service");
            default:
                return new Exception("Generic error: " + response.toString());
        }
    }
}
