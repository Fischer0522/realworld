package com.fischer.data;


import com.fasterxml.jackson.annotation.JsonRootName;
import com.fischer.service.user.constraintValidator.DuplicatedEmailConstraint;
import com.fischer.service.user.constraintValidator.DuplicatedUsernameConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@JsonRootName("user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserParam {
    @Builder.Default
    @Email(message = "请输入正确的邮箱格式")
    private String email="";

    @Builder.Default
    private String password="";
    @Builder.Default
    private String username="";
    @Builder.Default
    private String bio="";
    @Builder.Default
    private String image="";

}
