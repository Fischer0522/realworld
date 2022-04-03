package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("article")
@Builder
@Validated
public class UpdateArticleParam {
    @Builder.Default
    //@Max(value = 15,message = "标题最大长度为15")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5\\w,.，。‘’“”'()（）！!？?~]+$",message = "文章标题格式不正确，含特殊字符")
    private String title="";
    @Builder.Default
    //@Max(value =10000,message = "文章主体的最大长度为10000")
    private String body="";
    @Builder.Default
   // @Max(value = 200,message = "文章描述的最大长度为200")
    private String description="";
}
