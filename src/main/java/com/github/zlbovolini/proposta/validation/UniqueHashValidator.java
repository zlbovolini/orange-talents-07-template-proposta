package com.github.zlbovolini.proposta.validation;

import com.github.zlbovolini.proposta.comum.Dado;
import com.github.zlbovolini.proposta.exception.ApiFieldErrorException;
import com.github.zlbovolini.proposta.exception.FieldErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueHashValidator implements ConstraintValidator<UniqueHash, String> {

    private Class<?> entity;
    private String field;
    private String self;
    private HttpStatus status;
    private String message;
    private PasswordEncoder encoder;

    private final Logger logger = LoggerFactory.getLogger(UniqueHashValidator.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(UniqueHash constraintAnnotation) {
        entity = constraintAnnotation.entity();
        field = constraintAnnotation.field();
        self = constraintAnnotation.self();
        status = constraintAnnotation.statusOnFailure();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String dadoOriginal, final ConstraintValidatorContext context) {

        String hash = Dado.encode(dadoOriginal).hashed();

        StringBuilder stringBuilder = new StringBuilder()
                .append("SELECT COUNT(e) = 0 FROM ")
                .append(entity.getName())
                .append(" e WHERE e.")
                .append(field)
                .append(" = :hash");

        TypedQuery<Boolean> query = entityManager.createQuery(stringBuilder.toString(), Boolean.class);
        query.setParameter("hash", hash);

        boolean isUnique = query.getSingleResult();

        if (!isUnique) {
            throw new ApiFieldErrorException(status, List.of(new FieldErrorInfo(self, message)));
        }

        return true;
    }
}
