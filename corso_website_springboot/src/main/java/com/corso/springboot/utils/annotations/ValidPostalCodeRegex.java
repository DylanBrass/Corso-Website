package com.corso.springboot.utils.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPostalCodeRegex implements ConstraintValidator<ValidCanadianPostalCode, String> {
    @Override
    public void initialize(ValidCanadianPostalCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

            if (value == null) {
                return false;
            }

            String pattern = "(?i)^[abceghj-nprstvxy]\\d[abceghj-nprstv-z][ -]?\\d[abceghj-nprstv-z]\\d$";

            return value.matches(pattern);
    }
}
