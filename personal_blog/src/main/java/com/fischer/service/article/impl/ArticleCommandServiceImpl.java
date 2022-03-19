package com.fischer.service.article.impl;

import com.fischer.data.NewArticleParam;
import com.fischer.data.UpdateArticleParam;
import com.fischer.pojo.Article;
import com.fischer.pojo.User;
import com.fischer.repository.ArticleRepository;
import com.fischer.service.article.ArticleCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
@Service
@Validated
public class ArticleCommandServiceImpl implements ArticleCommandService {
    @Autowired
    private ArticleRepository articleRepository;
    @Override
    public Article createArticle(NewArticleParam newArticleParam ,User creator ) {
        String userId=creator.getId();
        Article article=
                new Article
                (       newArticleParam.getTitle(),
                        newArticleParam.getDescription(),
                        newArticleParam.getBody(),
                        newArticleParam.getTagList(),
                        newArticleParam.getImageList(),
                        userId
                );
        articleRepository.save(article);
        return article;
    }

    @Override
    public Article updateArticle(Article article,UpdateArticleParam updateArticleParam) {
        article.update(
                updateArticleParam.getTitle(),
                updateArticleParam.getDescription(),
                updateArticleParam.getBody()

        );
        articleRepository.save(article);
        return article;
    }
}
