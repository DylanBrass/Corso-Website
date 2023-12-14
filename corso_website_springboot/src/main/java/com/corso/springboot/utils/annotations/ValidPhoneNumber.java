package com.corso.springboot.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPhoneNumberRegex.class)
public @interface ValidPhoneNumber {
    String message() default "Invalid Phone Number Format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
