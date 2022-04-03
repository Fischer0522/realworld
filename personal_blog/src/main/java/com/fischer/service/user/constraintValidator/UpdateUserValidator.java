package com.fischer.service.user.constraintValidator;

import com.fischer.api.exception.BizException;
import com.fischer.pojo.User;
import com.fischer.repository.UserRepository;
import com.fischer.data.UpdateUserCommand;
import org.apache.logging.log4j.util.Strings;
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
        String inputPassword=value.getParam().getPassword();
        String inputBio=value.getParam().getBio();
        if(!inputEmail.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$" )){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("邮箱格式不正确")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }
        if(Strings.isNotEmpty(inputPassword)&&!inputPassword.matches("[a-zA-Z0-9-_]{3,20}")){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(("密码只能使用3-20位的字母或数字和-_"))
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }
        if(Strings.isNotEmpty(inputUsername)&&!inputUsername.matches("[\\u4e00-\\u9fa5_a-zA-Z0-9_-]{1,20}")){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(("用户名只支持20位以内的中英文和数字以及下划线"))
                    .addPropertyNode("username")
                    .addConstraintViolation();
            return false;
        }
        if(Strings.isNotEmpty(inputBio)&&inputBio.length()>30){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(("个性签名的最大长度为30"))
                    .addPropertyNode("bio")
                    .addConstraintViolation();
            return false;
        }
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
