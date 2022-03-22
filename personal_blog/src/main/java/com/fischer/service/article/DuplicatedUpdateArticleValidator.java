package com.fischer.service.article;

import com.fischer.api.exception.BizException;
import com.fischer.data.ArticleData;
import com.fischer.data.UpdateArticleCommand;
import com.fischer.pojo.Article;
import com.fischer.readService.ArticleReadService;
import com.fischer.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class DuplicatedUpdateArticleValidator implements ConstraintValidator<DuplicatedUpdateArticleConstraint, UpdateArticleCommand> {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public boolean isValid(UpdateArticleCommand value ,ConstraintValidatorContext context) {

        String title = value.getUpdateArticleParam().getTitle();
        String slug = Article.toSlug(title);
        final Article targetarticle = value.getTargetArticle();
        Boolean isValid = articleRepository.findBySlug(slug)
                .map(article -> article.equals(targetarticle))
                .orElse(true);


        if(isValid) {
            return true;
        }
        else{
            context.disableDefaultConstraintViolation();

                context.buildConstraintViolationWithTemplate("该文章标题已经有人使用了，换一个吧~")
                        .addPropertyNode("title")
                        .addConstraintViolation();
                return false;
        }


    }
}
