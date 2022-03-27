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
  /*  @Test
    public void testCreateNew(){
        List<String> tag=new LinkedList<>();
        tag.add("noob");
        tag.add("game");
        tag.add("fps");
        List<String> image=new LinkedList<>();
        image.add("123.jpg");
        image.add("1222.jps");
        NewArticleParam articleParam=new NewArticleParam(
                "我学你",
                "吸欧帝",
                "年货",
                tag,
                null
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



    }*/
}
