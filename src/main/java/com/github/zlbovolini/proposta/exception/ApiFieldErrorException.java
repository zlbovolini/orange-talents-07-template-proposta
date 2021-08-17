package com.github.zlbovolini.proposta.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiFieldErrorException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final List<FieldErrorInfo> errors;

    public ApiFieldErrorException(HttpStatus httpStatus, List<FieldErrorInfo> errors) {
        super("Invalid field exception");
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public List<FieldErrorInfo> getErrors() {
        return errors;
    }
}
