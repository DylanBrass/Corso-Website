package com.corso.springboot.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPostalCodeRegex.class)
public @interface ValidCanadianPostalCode {
    String message() default "Invalid Postal Code Format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
