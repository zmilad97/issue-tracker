package com.github.zmilad97.bugtracker.validator;

import com.github.zmilad97.bugtracker.annotation.ProjectExist;
import com.github.zmilad97.bugtracker.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProjectValidator implements ConstraintValidator<ProjectExist, Integer> {
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return projectRepository.existsById(integer);
    }

    @Override
    public void initialize(ProjectExist constraintAnnotation) {

    }
}
