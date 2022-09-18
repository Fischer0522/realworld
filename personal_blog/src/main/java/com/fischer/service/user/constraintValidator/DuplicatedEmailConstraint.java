package com.fischer.service.user.constraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = DuplicatedEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedEmailConstraint {
    String message() default "该邮箱已被人注册";

    Class<?>[] groups() default {};

    Class<? extends Payload> [] payload() default {};
}
