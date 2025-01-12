package com.api.gateway.exception;

public class DownstreamServiceException extends RuntimeException {

    public DownstreamServiceException(String s)
    {
        super(s);
    }

}
