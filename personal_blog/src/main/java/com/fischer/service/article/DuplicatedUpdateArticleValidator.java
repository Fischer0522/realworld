package com.fischer.service.article;

import com.fischer.api.exception.BizException;
import com.fischer.data.ArticleData;
import com.fischer.data.UpdateArticleCommand;
import com.fischer.pojo.Article;
import com.fischer.readService.ArticleReadService;
import com.fischer.repository.ArticleRepository;
import org.apache.logging.log4j.util.Strings;
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
        String description = value.getUpdateArticleParam().getDescription();
        String body = value.getUpdateArticleParam().getBody();
        /*if(title.length()>15){
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate("文章标题最大长度为15")
                    .addPropertyNode("title")
                    .addConstraintViolation();
            return false;

        }*/
        /*if(!title.matches("^[\\u4e00-\\u9fa5\\w,.，。‘’“”'()（）！!？?~]+$")){
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate("文章标题格式不正确，含特殊字符")
                    .addPropertyNode("title")
                    .addConstraintViolation();
            return false;

        }*/
        /*if(Strings.isNotEmpty(description)&&description.length()>30){
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate("文章描述最多为200个字符")
                    .addPropertyNode("description")
                    .addConstraintViolation();
            return false;
        }*/
        /*if(Strings.isNotEmpty(body)&&body.length()>10000){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("文章主体最多为10000个字符")
                    .addPropertyNode("body")
                    .addConstraintViolation();
            return false;

        }*/
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
