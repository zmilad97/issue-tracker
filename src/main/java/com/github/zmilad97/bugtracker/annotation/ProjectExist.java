package com.github.zmilad97.bugtracker.annotation;

import com.github.zmilad97.bugtracker.validator.ProjectValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ProjectValidator.class)
public @interface ProjectExist {
    String message() default "Selected Project Is Not Valid !";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
