package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@JsonRootName("comment")
public class NewCommentParam {
    @NotBlank(message = "真的不再写几个字吗")
    @Length(max = 200,message = "最大长度为200个字符")
    private String body;
}
