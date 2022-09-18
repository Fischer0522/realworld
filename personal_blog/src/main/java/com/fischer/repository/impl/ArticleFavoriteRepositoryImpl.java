package com.fischer.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.ArticleFavoriteDao;
import com.fischer.pojo.ArticleFavorite;
import com.fischer.repository.ArticleFavoriteRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ArticleFavoriteRepositoryImpl implements ArticleFavoriteRepository {
    @Autowired
    private ArticleFavoriteDao articleFavoriteDao;
    @Override
    public void save(ArticleFavorite articleFavorite) {
        LambdaQueryWrapper<ArticleFavorite> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(articleFavorite.getArticleId()),ArticleFavorite::getArticleId,articleFavorite.getArticleId());
        lqw.eq(Strings.isNotEmpty(articleFavorite.getUserId()),ArticleFavorite::getUserId,articleFavorite.getUserId());
        ArticleFavorite articleFavorite1 = articleFavoriteDao.selectOne(lqw);
        if(articleFavorite1==null)
        {
            articleFavoriteDao.insert(articleFavorite);
        }


    }

    @Override
    public Optional<ArticleFavorite> find(String articleId, String userId) {
        LambdaQueryWrapper<ArticleFavorite> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(articleId),ArticleFavorite::getArticleId,articleId);
        lqw.eq(Strings.isNotEmpty(userId),ArticleFavorite::getUserId,userId);
        ArticleFavorite articleFavorite = articleFavoriteDao.selectOne(lqw);
        return Optional.ofNullable(articleFavorite);
    }

    @Override
    public void remove(ArticleFavorite favorite) {
        LambdaQueryWrapper<ArticleFavorite> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(favorite.getArticleId()),ArticleFavorite::getArticleId,favorite.getArticleId());
        lqw.eq(Strings.isNotEmpty(favorite.getUserId()),ArticleFavorite::getUserId,favorite.getUserId());
        ArticleFavorite articleFavorite = articleFavoriteDao.selectOne(lqw);
        if(articleFavorite!=null)
        {
            articleFavoriteDao.delete(lqw);
        }

    }
}
