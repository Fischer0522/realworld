package com.fischer.data;


import com.fasterxml.jackson.annotation.JsonRootName;
import com.fischer.service.user.constraintValidator.DuplicatedEmailConstraint;
import com.fischer.service.user.constraintValidator.DuplicatedUsernameConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@JsonRootName("user")
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class RegisterParam {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "请填写格式正确的邮箱")
    @DuplicatedEmailConstraint
    private String email;

    @NotBlank(message = "请给自己起个名字")
    @DuplicatedUsernameConstraint
    @Pattern(regexp = "[\\u4e00-\\u9fa5_a-zA-Z0-9_-]{1,20}")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "[a-zA-Z0-9-_]{3,20}",message = "密码只能使用3-20位的字母或数字和-_")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
