package com.api.gateway.exception;

import feign.FeignException;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{\"error\": \"Bad Request\", \"message\": \"" + ex.getMessage() + "\"}");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"Not Found\", \"message\": \"" + ex.getMessage() + "\"}");
    }

    @ExceptionHandler(DownstreamServiceException.class)
    public ResponseEntity<String> handleDownstreamError(DownstreamServiceException ex) {
        log.info("handleDownstreamError.......");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Downstream Error\", \"message\": \"" + ex.getMessage() + "\"}");
    }
    /*@ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<String> handleNotAuthorized(UnAuthorizedException ex) {
        log.info("handleNotAuthorized....");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"error\": \"Not Authorized\", \"message\": \"" + ex.getMessage() + "\"}");
    }*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Internal Error\", \"message\": \"" + ex.getMessage() + "\"}");
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public Mono<ResponseEntity<String>> handleUnAuthorizedException(UnAuthorizedException ex) {
        log.info("handleUnAuthorizedException.......");
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Internal Error\", \"message\": \"" + ex.getMessage() + "\"}"));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Feign error occurred: " + ex.getMessage());
    }
}

