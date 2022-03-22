package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("user")
public class LoginParam {

    @Email(message = "请输入格式正确的邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "密码不能为空")
    private String password;

}
