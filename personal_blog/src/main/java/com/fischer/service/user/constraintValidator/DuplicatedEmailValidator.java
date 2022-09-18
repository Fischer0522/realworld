package com.fischer.service.user.constraintValidator;

import com.fischer.repository.UserRepository;
import com.fischer.service.user.constraintValidator.DuplicatedEmailConstraint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DuplicatedEmailValidator implements ConstraintValidator<DuplicatedEmailConstraint,String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return (!userRepository.findByEmail(value).isPresent()
                ||value.isEmpty()
                ||value==null);

    }
}
