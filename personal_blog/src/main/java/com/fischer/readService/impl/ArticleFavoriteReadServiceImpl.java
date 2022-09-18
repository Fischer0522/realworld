package com.fischer.readService.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.ArticleFavoriteDao;
import com.fischer.data.ArticleFavoriteCount;
import com.fischer.pojo.ArticleFavorite;
import com.fischer.pojo.Tag;
import com.fischer.pojo.User;
import com.fischer.readService.ArticleFavoriteReadService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
@Repository
public class ArticleFavoriteReadServiceImpl implements ArticleFavoriteReadService {
    @Autowired
    private ArticleFavoriteDao articleFavoriteDao;
    @Override
    public boolean isUserfavorite(String userId, String articleId) {
        LambdaQueryWrapper<ArticleFavorite> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(userId),ArticleFavorite::getUserId,userId);
        lqw.eq(Strings.isNotEmpty(articleId),ArticleFavorite::getArticleId,articleId);
        ArticleFavorite articleFavorite = articleFavoriteDao.selectOne(lqw);
        if(articleFavorite==null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public int articleFavoriteCount(String articleId) {
        LambdaQueryWrapper<ArticleFavorite>lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(articleId),ArticleFavorite::getArticleId,articleId);
        return articleFavoriteDao.selectCount(lqw);
    }

    @Override
    public List<ArticleFavoriteCount> articlesFavoriteCount(List<String> ids) {
        List<ArticleFavoriteCount> articleFavoriteCounts=new LinkedList<>();
        for(String id :ids)
        {
            int count = articleFavoriteCount(id);
            ArticleFavoriteCount articleFavoriteCount=new ArticleFavoriteCount(id,count);
            articleFavoriteCounts.add(articleFavoriteCount);
        }
        return articleFavoriteCounts;
    }

    @Override
    public Set<String> userFavorites(List<String> ids, User currentUser) {
        String userId=currentUser.getId();
        Set<String> articles=new HashSet<>();

        for(String id:ids)
        {
            LambdaQueryWrapper<ArticleFavorite> lqw=new LambdaQueryWrapper<>();
            lqw.eq(Strings.isNotEmpty(userId),ArticleFavorite::getUserId,userId);
            lqw.eq(Strings.isNotEmpty(id),ArticleFavorite::getArticleId,id);
            ArticleFavorite articleFavorite = articleFavoriteDao.selectOne(lqw);
            if(articleFavorite!=null)
            {
                articles.add(articleFavorite.getArticleId());
            }
        }
        return articles;
    }
}
