package com.github.zlbovolini.proposta.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ApiErrorResponse {

    private final List<String> globalErrors = new ArrayList<>();
    private final List<FieldErrorInfo> errors = new ArrayList<>();

    ApiErrorResponse() {}

    void addFieldError(FieldErrorInfo fieldErrorInfo) {
        errors.add(fieldErrorInfo);
    }

    void addGlobalError(String errorMessage) {
        globalErrors.add(errorMessage);
    }

    public List<String> getGlobalErrors() {
        return Collections.unmodifiableList(globalErrors);
    }

    public List<FieldErrorInfo> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
