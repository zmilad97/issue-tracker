package com.github.zmilad97.bugtracker.validator;

import com.github.zmilad97.bugtracker.annotation.PasswordMatches;
import com.github.zmilad97.bugtracker.dtos.UserDto;
import com.github.zmilad97.bugtracker.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UserDto user = (UserDto) obj;
        return user.getPassword().equals(user.getPassword2());
    }
}
