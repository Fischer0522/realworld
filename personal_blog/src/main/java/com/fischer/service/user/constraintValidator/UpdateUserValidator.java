package com.fischer.service.user.constraintValidator;

import com.fischer.api.exception.BizException;
import com.fischer.pojo.User;
import com.fischer.repository.UserRepository;
import com.fischer.data.UpdateUserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UpdateUserValidator implements ConstraintValidator<UpdateUserConstraint, UpdateUserCommand> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(UpdateUserCommand value, ConstraintValidatorContext context) {
        String inputEmail=value.getParam().getEmail();
        String inputUsername=value.getParam().getUsername();
        final User targetUser=value.getTargetUser();
        boolean isEmailValid =
                userRepository.findByEmail(inputEmail)
                        .map(user -> user.equals(targetUser))
                        .orElse(true);

        boolean isUsernameValid =
                userRepository.findByUsername(inputUsername)
                        .map(user -> user.equals(targetUser))
                        .orElse(true);

        if(isEmailValid&&isUsernameValid){
            return true;
        }
        else {
            context.disableDefaultConstraintViolation();
            if(!isEmailValid){
                context.buildConstraintViolationWithTemplate("该邮箱已被别人注册")
                        .addPropertyNode("email")
                        .addConstraintViolation();
               // throw new BizException(HttpStatus.BAD_REQUEST,"该邮箱已被别人注册");
            }
            if(!isUsernameValid){
                //throw new BizException(HttpStatus.BAD_REQUEST,"该用户名已经被别人所使用");
                context.buildConstraintViolationWithTemplate("该用户名已经被别人使用")
                        .addPropertyNode("username")
                        .addConstraintViolation();

            }
            return false;
        }

    }

}
