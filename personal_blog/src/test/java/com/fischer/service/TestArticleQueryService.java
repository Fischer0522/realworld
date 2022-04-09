package com.fischer.service;

import com.fischer.assistant.MyPage;
import com.fischer.dao.UserDao;
import com.fischer.data.ArticleData;
import com.fischer.data.ArticleDataList;
import com.fischer.pojo.User;
import com.fischer.service.article.ArticleQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class TestArticleQueryService {
    @Autowired
    private ArticleQueryService articleQueryService;
    @Autowired
    private UserDao userDao;
    @Test
    public void testFindById(){
        User user = userDao.selectById("1234");
        String articleId="e9fb9c05-ca78-4c52-83b7-f6043f352bf9";
        Optional<ArticleData> articleData = articleQueryService.findById(articleId, user);
        System.out.println(articleData.get());
    }
    @Test
    public void testFindBySlug(){
        User user = userDao.selectById("1234");
        String slug="call-of-duty";
        Optional<ArticleData> articleData = articleQueryService.findBySlug(slug, user);
        System.out.println(articleData.get());

    }
    /*ArticleDataList findRecentAticles(
             String tag,
             String author,
             String favoritedBy,
             MyPage myPage,
             User currentUser);*/
    @Test
    public void testFindRecentAticles(){
        String tag="noob";
        String author="fischer";
        String favoritedBy="fischer";
        MyPage myPage=new MyPage();
        User user = userDao.selectById("1234");
        ArticleDataList recentArticles = articleQueryService.findRecentArticles(null, null,null, myPage, null);
        System.out.println(recentArticles);
    }

}
