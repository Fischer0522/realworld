package com.fischer.repository;

import com.fischer.pojo.ArticleFavorite;
import com.fischer.repository.ArticleFavoriteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class TestArticleFavoriteRepository {
    @Autowired
    private ArticleFavoriteRepository articleFavoriteRepository;
    @Test
    public void testSave(){
        ArticleFavorite articleFavorite=new ArticleFavorite("e9fb9c05-ca78-4c52-83b7-f6043f352bf9","3733");
        articleFavoriteRepository.save(articleFavorite);

    }
    @Test
    public void testFind(){
        String articleId="e30e99ae-5bdf-4a3f-90fa-49cd18692eac";
        String userId="1234";
        Optional<ArticleFavorite> articleFavorite = articleFavoriteRepository.find(articleId, userId);
        System.out.println(articleFavorite.get());
    }
    @Test
    public void testRemove(){
        ArticleFavorite articleFavorite=new ArticleFavorite("e30e99ae-5bdf-4a3f-90fa-49cd18692eac","1234");
        articleFavoriteRepository.remove(articleFavorite);
    }
}
