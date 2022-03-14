package com.fischer.dao;

import com.fischer.assistant.MyPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestArticleDao {
    @Autowired
    private ArticleDao articleDao;
    @Test
    public void testQueryArticle(){
        MyPage myPage=new MyPage(0,5);
        for (String queryArticle : articleDao.queryArticles(null,null, "fischer", myPage)) {
            System.out.println(queryArticle);
        }
    }
    @Test
    public void testCountArticle(){
        Integer noob = articleDao.countArticle("noob", null, null);
        System.out.println(noob);
    }

}
