package com.github.zlbovolini.proposta.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Base64;

public class Base64EncodedValidator implements ConstraintValidator<Base64Encoded, String> {

    @Override
    public boolean isValid(String hash, ConstraintValidatorContext constraintValidatorContext) {

        try {
            Base64.getDecoder().decode(hash);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
