package com.fischer.service.article;

import com.fischer.data.ArticleData;
import com.fischer.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class DuplicatedUpdateArticleValidator implements ConstraintValidator<DuplicatedUpdateArticleConstraint,String> {
    @Autowired
    private ArticleQueryService articleQueryService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !articleQueryService.findBySlug(Article.toSlug(value),null).isPresent();




        /*String inputEmail=value.getParam().getEmail();
        String inputUsername=value.getParam().getUsername();
        final User targetUser=value.getTargetUser();
        boolean isEmailValid =
                userRepository.findByEmail(inputEmail)
                        .map(user -> user.equals(targetUser))
                        .orElse(true);

        boolean isUsernameValid =
                userRepository.findByUsername(inputUsername)
                        .map(user -> user.equals(targetUser))
                        .orElse(true);

        if(isEmailValid&&isUsernameValid){
            return true;
        }*/
    }
}
