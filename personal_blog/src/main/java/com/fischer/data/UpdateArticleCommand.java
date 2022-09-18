package com.fischer.data;

import com.fischer.pojo.Article;
import com.fischer.service.article.DuplicatedUpdateArticleConstraint;
import com.fischer.service.user.constraintValidator.UpdateUserConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@DuplicatedUpdateArticleConstraint
public class UpdateArticleCommand {
    private Article targetArticle;

    private UpdateArticleParam updateArticleParam;

}
