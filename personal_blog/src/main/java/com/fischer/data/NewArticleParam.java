package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fischer.service.article.DuplicatedArticleConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("article")
public class NewArticleParam {
    @NotBlank(message = "不给文章起一个标题吗")
    @DuplicatedArticleConstraint
    @Pattern(regexp = "^[\\u4e00-\\u9fa5\\w,.，。‘’“”'()（）！!？?~]+$",message = "文章标题格式不正确，含特殊字符")
    @Length(max = 15,message = "文章最大长度为15个字符")
    private String title;

    @NotBlank(message = "文章描述不能为空")
    @Length(max = 200,message = "最大长度为200个字符")
    private String description;

    @NotBlank(message = "真的不再写几个字吗")
    @Length(max = 10000,message = "最大长度为10000个字符")
    private String body;

    private List<String> tagList=new LinkedList<>();

    private List<String> imageList=new LinkedList<>();

}
