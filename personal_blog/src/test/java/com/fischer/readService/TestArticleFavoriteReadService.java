package com.fischer.readService;

import com.fischer.dao.UserDao;
import com.fischer.data.ArticleFavoriteCount;
import com.fischer.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class TestArticleFavoriteReadService {
    @Autowired
    private ArticleFavoriteReadService articleFavoriteReadService;
    @Autowired
    private UserDao userDao;
    @Test
    public void testIsUserFavorite() {
        String userId="3744";
        String articleId="e9fb9c05-ca78-4c52-83b7-f6043f352bf9";
        boolean userfavorite = articleFavoriteReadService.isUserfavorite(userId, articleId);
        System.out.println(userfavorite);

    }

    @Test
    public void testArticleFavoriteCount(){
        String articleId="e9fb9c05-ca78-4c52-83b7-f6043f352bf9";
        int count = articleFavoriteReadService.articleFavoriteCount(articleId);
        System.out.println(count);

    }
    @Test
    public void testArticlesFavoriteCount(){
        List<String> ids=new LinkedList<>();
        ids.add("e9fb9c05-ca78-4c52-83b7-f6043f352bf9");
        ids.add("e30e99ae-5bdf-4a3f-90fa-49cd18692eac");
        ids.add("f0daa5e9-99ed-4eb8-929a-ab326207b397");
        for (ArticleFavoriteCount articleFavoriteCount : articleFavoriteReadService.articlesFavoriteCount(ids)) {
            System.out.println(articleFavoriteCount);
        }
    }
    @Test
    public void testUserFavorites(){
        List<String> ids=new LinkedList<>();
        ids.add("e9fb9c05-ca78-4c52-83b7-f6043f352bf9");
        ids.add("e30e99ae-5bdf-4a3f-90fa-49cd18692eac");
        ids.add("f0daa5e9-99ed-4eb8-929a-ab326207b397");
        ids.add("13977f4d-b541-4252-b6c9-6a220f3a07ca");
        User user = userDao.selectById("1234");
        Set<String> favorites = articleFavoriteReadService.userFavorites(ids, user);
        System.out.println(favorites);
    }
}
