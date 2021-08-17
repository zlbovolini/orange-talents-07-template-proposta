package com.github.zlbovolini.proposta.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class })
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(BindException e) {

        List<ObjectError> globalErrors = e.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        globalErrors.forEach(globalError -> {
            errorResponse.addGlobalError(getErrorMessage(globalError));
        });

        fieldErrors.forEach(fieldError -> {
            String field = fieldError.getField();
            String message = getErrorMessage(fieldError);

            errorResponse.addFieldError(new FieldErrorInfo(field, message));
        });

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException e) {

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        e.getConstraintViolations().forEach(violation -> {

            String field = "";
            for(Path.Node node : violation.getPropertyPath()) {
                field = node.getName();
            }

            // !todo Corrigir resolução da mensagem de erro
            errorResponse.addFieldError(new FieldErrorInfo(field, violation.getMessage()));
        });

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ApiErrorException.class)
    private ResponseEntity<ApiErrorResponse> handleApiError(ApiErrorException apiErrorException) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();

        apiErrorResponse.addGlobalError(apiErrorException.getMessage());

        return ResponseEntity.status(apiErrorException.getHttpStatus()).body(apiErrorResponse);
    }

    @ExceptionHandler(ApiFieldErrorException.class)
    private ResponseEntity<ApiErrorResponse> handleApiFieldError(ApiFieldErrorException apiErrorException) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();

        apiErrorException.getErrors().forEach(apiErrorResponse::addFieldError);

        return ResponseEntity.status(apiErrorException.getHttpStatus()).body(apiErrorResponse);
    }

    private String getErrorMessage(MessageSourceResolvable error) {
        return messageSource.getMessage(error, LocaleContextHolder.getLocale());
    }
}
