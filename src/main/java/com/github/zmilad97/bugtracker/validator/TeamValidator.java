package com.github.zmilad97.bugtracker.validator;

import com.github.zmilad97.bugtracker.annotation.ProjectExist;
import com.github.zmilad97.bugtracker.annotation.TeamExist;
import com.github.zmilad97.bugtracker.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TeamValidator implements ConstraintValidator<TeamExist, Integer> {

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return teamRepository.existsById(integer);
    }

    @Override
    public void initialize(TeamExist constraintAnnotation) {

    }
}
