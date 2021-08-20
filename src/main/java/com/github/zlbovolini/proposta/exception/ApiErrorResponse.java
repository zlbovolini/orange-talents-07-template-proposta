package com.github.zlbovolini.proposta.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiErrorResponse {

    private final List<String> globalErrors = new ArrayList<>();
    private final List<FieldErrorInfo> errors = new ArrayList<>();

    ApiErrorResponse() {}

    public static ApiErrorResponse builder() {
        return new ApiErrorResponse();
    }

    public ApiErrorResponse addFieldError(FieldErrorInfo fieldErrorInfo) {
        errors.add(fieldErrorInfo);
        return this;
    }

    public ApiErrorResponse addGlobalError(String errorMessage) {
        globalErrors.add(errorMessage);
        return this;
    }

    public List<String> getGlobalErrors() {
        return Collections.unmodifiableList(globalErrors);
    }

    public List<FieldErrorInfo> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
