package com.fischer.service.article;

import com.fischer.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class DuplicatedArticleValidator
implements ConstraintValidator<DuplicatedArticleConstraint,String> {
    @Autowired
    private ArticleQueryService articleQueryService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !articleQueryService.findBySlug(Article.toSlug(value),null).isPresent();

    }
}
