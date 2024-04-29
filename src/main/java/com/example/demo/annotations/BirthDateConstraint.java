package com.example.demo.annotations;

import com.example.demo.BirthDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDateConstraint {


    String message() default "User is too young";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
