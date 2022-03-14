package com.fischer.service;

import com.fischer.data.NewArticleParam;
import com.fischer.data.UpdateArticleParam;
import com.fischer.pojo.Article;
import com.fischer.pojo.User;
import com.fischer.repository.ArticleRepository;
import com.fischer.repository.UserRepository;
import com.fischer.service.article.ArticleCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TestArticleCommandService {
    @Autowired
    private ArticleCommandService articleCommandService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Test
    public void testCreateNew(){
        List<String> tag=new LinkedList<>();
        tag.add("noob");
        tag.add("game");
        tag.add("fps");
        NewArticleParam articleParam=new NewArticleParam(
                "call of duty",
                "吸欧帝",
                "年货",
                tag
        );
        Optional<User> fishcer = userRepository.findByUsername("fischer");
        User user=fishcer.get();
        Article article = articleCommandService.createArticle(articleParam, user);
        System.out.println(article);

    }
    @Test
    public void testUpdate(){
        Optional<Article> batttlefeild1 = articleRepository.findBySlug("batttlefeild1");
        Article article = batttlefeild1.get();
        UpdateArticleParam updateArticleParam=new UpdateArticleParam(
                null,
                null,
                "薯条来咯"
        );
        Article article1 = articleCommandService.updateArticle(article, updateArticleParam);
        System.out.println(article1);


    }
}
