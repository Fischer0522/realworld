package com.fischer.readService;

import com.fischer.data.ArticleData;
import com.fischer.pojo.Article;
import com.fischer.pojo.User;
import com.fischer.readService.ArticleReadService;
import com.fischer.repository.ArticleRepository;
import com.fischer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TestArticleReadService {
    @Autowired
    private ArticleReadService articleReadService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    public void testFindArticles(){
        List<String> articleIds=new LinkedList<>();
        articleIds.add("e30e99ae-5bdf-4a3f-90fa-49cd18692eac");
        articleIds.add("e9fb9c05-ca78-4c52-83b7-f6043f352bf9");
        articleIds.add("f0daa5e9-99ed-4eb8-929a-ab326207b397");

        for (ArticleData article : articleReadService.findArticles(articleIds)) {
            System.out.println(article);
        }

    }
    @Test
    public void testFindById(){
        ArticleData articleData = articleReadService.findById("e9fb9c05-ca78-4c52-83b7-f6043f352bf9");
        System.out.println(articleData);



    }
    @Test
    public void testFindBySlug(){
        ArticleData articleData = articleReadService.findBySlug("how-can-i-learn");
        System.out.println(articleData);

    }


}
