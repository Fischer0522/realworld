package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fischer.service.article.DuplicatedArticleConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("article")
public class NewArticleParam {
    @NotBlank(message = "不给文章起一个标题吗")
    @DuplicatedArticleConstraint
    private String title;

    @NotBlank(message = "文章描述不能为空")
    private String description;

    @NotBlank(message = "真的不再写几个字吗")
    private String body;


    private List<String> tagList=new LinkedList<>();

}
