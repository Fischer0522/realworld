package com.fischer.service.article;

import com.fischer.data.NewArticleParam;
import com.fischer.data.UpdateArticleParam;
import com.fischer.pojo.Article;
import com.fischer.pojo.User;

import javax.validation.Valid;

public interface ArticleCommandService {
    Article createArticle(@Valid NewArticleParam newArticleParam, User creator);
    Article updateArticle(Article article,@Valid UpdateArticleParam updateArticleParam);

}
