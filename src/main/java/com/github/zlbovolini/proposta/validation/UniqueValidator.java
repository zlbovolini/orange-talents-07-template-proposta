package com.github.zlbovolini.proposta.validation;

import com.github.zlbovolini.proposta.exception.ApiFieldErrorException;
import com.github.zlbovolini.proposta.exception.FieldErrorInfo;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    private Class<?> entity;
    private String field;
    private String self;
    private HttpStatus status;
    private String message;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(Unique constraintAnnotation) {
        entity = constraintAnnotation.entity();
        field = constraintAnnotation.field();
        self = constraintAnnotation.self();
        status = constraintAnnotation.statusOnFailure();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object fieldValue, final ConstraintValidatorContext context) {

        StringBuilder stringBuilder = new StringBuilder()
                .append("SELECT COUNT(e) = 0 FROM ")
                .append(entity.getName())
                .append(" e WHERE e.")
                .append(field)
                .append(" = :fieldValue");
        System.out.println("ASDASASDSA");

        TypedQuery<Boolean> query = entityManager.createQuery(stringBuilder.toString(), Boolean.class);
        query.setParameter("fieldValue", fieldValue);

        boolean isValid = query.getSingleResult();

        if (status.equals(HttpStatus.BAD_REQUEST)) {
            return isValid;
        }

        throw new ApiFieldErrorException(status, List.of(new FieldErrorInfo(self, message)));
    }
}
