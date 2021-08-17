package com.github.zlbovolini.proposta.exception;

public class FieldErrorInfo {

    private final String field;
    private final String message;

    public FieldErrorInfo(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
