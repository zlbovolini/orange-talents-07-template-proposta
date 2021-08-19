package com.github.zlbovolini.proposta.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = Base64EncodedValidator.class)
public @interface Base64Encoded {

    String message() default "Deve ser um valor codificado em Base64 válido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
