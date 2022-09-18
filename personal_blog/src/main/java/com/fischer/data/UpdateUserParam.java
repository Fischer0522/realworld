package com.fischer.data;


import com.fasterxml.jackson.annotation.JsonRootName;
import com.fischer.service.user.constraintValidator.DuplicatedEmailConstraint;
import com.fischer.service.user.constraintValidator.DuplicatedUsernameConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@JsonRootName("user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class UpdateUserParam {
    @Builder.Default
    @Email(message = "邮箱格式不正确")
    private String email="";

    @Pattern(regexp = "[a-zA-Z0-9-_]{3,20}",message = "密码只能使用3-20位的字母或数字和-_")
    @Builder.Default
    private String password="";

    @Builder.Default
    @Pattern(regexp = "[\\u4e00-\\u9fa5_a-zA-Z0-9-]{1,20}",message ="用户名只支持20位以内的中英文和数字以及下划线" )
    private String username="";

    @Builder.Default
    @Length(max = 30, message = "个性签名的长度最大为30")
    private String bio="";

    @Builder.Default
    private String image="";

}
