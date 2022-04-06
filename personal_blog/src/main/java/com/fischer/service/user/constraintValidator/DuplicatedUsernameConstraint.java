package com.fischer.service.user.constraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = DuplicatedUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedUsernameConstraint {
    String message()default "该用户名已经被人使用";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

}
