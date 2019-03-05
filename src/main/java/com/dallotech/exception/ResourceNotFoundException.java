package com.dallotech.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String customMessage){
        super(customMessage);
    }
}
