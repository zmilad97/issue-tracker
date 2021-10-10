package com.github.zmilad97.bugtracker.annotation;

import com.github.zmilad97.bugtracker.validator.ProjectValidator;
import com.github.zmilad97.bugtracker.validator.TeamValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = TeamValidator.class)
public @interface TeamExist {

    String message() default "Selected Team Is Not Valid !";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
