package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@JsonRootName("comment")
public class NewCommentParam {
    @NotBlank(message = "真的不再写几个字吗")
    private String body;
}
