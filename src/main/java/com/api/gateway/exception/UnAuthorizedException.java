package com.api.gateway.exception;

public class UnAuthorizedException extends RuntimeException{

    public UnAuthorizedException(String s)
    {
        super(s);
    }
}
