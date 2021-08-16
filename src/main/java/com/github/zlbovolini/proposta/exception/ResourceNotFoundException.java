package com.github.zlbovolini.proposta.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    private ResourceNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
