package com.api.gateway.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String s)
    {
        super(s);
    }
}
