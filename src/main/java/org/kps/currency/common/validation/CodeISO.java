package org.kps.currency.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CodeISOValidator.class)
@Documented
public @interface CodeISO {
    String message() default "The input must be CodeISO";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
