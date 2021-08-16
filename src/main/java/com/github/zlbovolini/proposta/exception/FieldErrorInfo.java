package com.github.zlbovolini.proposta.exception;

class FieldErrorInfo {

    private final String field;
    private final String message;

    FieldErrorInfo(String field, String message) {
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
