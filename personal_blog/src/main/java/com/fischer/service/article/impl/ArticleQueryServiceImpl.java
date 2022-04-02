package com.fischer.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fischer.api.exception.BizException;
import com.fischer.assistant.MyPage;
import com.fischer.dao.ArticleDao;
import com.fischer.data.ArticleData;
import com.fischer.data.ArticleDataList;
import com.fischer.data.ArticleFavoriteCount;
import com.fischer.data.NewArticleParam;
import com.fischer.pojo.Article;
import com.fischer.pojo.User;
import com.fischer.readService.ArticleFavoriteReadService;
import com.fischer.readService.ArticleReadService;
import com.fischer.service.article.ArticleQueryService;
import org.apache.logging.log4j.util.Strings;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service

public class ArticleQueryServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleQueryService {

    private ArticleReadService articleReadService;
    private ArticleFavoriteReadService articleFavoriteReadService;
    private ArticleDao articleDao;
    @Autowired
    public ArticleQueryServiceImpl(ArticleReadService articleReadService,
                                   ArticleFavoriteReadService articleFavoriteReadService,
                                    ArticleDao articleDao )
    {
        this.articleFavoriteReadService=articleFavoriteReadService;
        this.articleReadService=articleReadService;
        this.articleDao=articleDao;
    }
    @Override
    public Optional<ArticleData> findById(String id, User user) {
        ArticleData articleData = articleReadService.findById(id);
        if(articleData==null){
            return Optional.empty();
        }
        else {
            if(user!=null){
                fillExtreInfo(id,user,articleData);
            }
            return Optional.of(articleData);
        }
    }

    @Override
    public Optional<ArticleData> findBySlug(String slug, User user) {
        ArticleData articleData = articleReadService.findBySlug(slug);
        if(articleData==null){
            return Optional.empty();
        }
        else{
            if(user!=null){
                fillExtreInfo(articleData.getId(),user,articleData);
            }
            return Optional.of(articleData);
        }
    }

    @Override
    public ArticleDataList findRecentArticles(String tag, String author, String favoritedBy, MyPage myPage, User currentUser) {
        List<String> articleIds = articleDao.queryArticles(tag,author,favoritedBy,myPage);
        Integer articleCount = articleDao.countArticle(tag, author, favoritedBy);
        if(articleIds.isEmpty()) {

            return new ArticleDataList(new ArrayList<>(),articleCount);
        }
        else{
            List<ArticleData> articles = articleReadService.findArticles(articleIds);
            if(currentUser!=null){
                fillExtraInfo(articles,currentUser);
            }

            return new ArticleDataList(articles,articleCount);

        }

    }

    @Override
    public ArticleDataList findRecentArticlesFuzzy(String title, String description, MyPage myPage, User currentUser) {
        List<ArticleData> articleData = articleReadService.finArticlesFuzzy(
                title,
                description,
                myPage
        );
        if (articleData.isEmpty()){
            return new ArticleDataList(new ArrayList<>(),0);
        }
        else{
            List<String> articleIds=articleData.stream().map(ArticleData::getId).collect(toList());
            Integer articleCount=articleData.size();
            if(currentUser!=null){
                fillExtraInfo(articleData,currentUser);
            }
            return new ArticleDataList(articleData,articleCount);
        }


    }

    @Override
    public void fillExtraInfo(List<ArticleData> articles, User currentUser) {
        setFavoriteCount(articles);
        if(currentUser!=null) {
            setIsfavorite(articles,currentUser);
        }

    }

    @Override
    public void fillExtreInfo(String id, User user, ArticleData articleData) {
        articleData.setFavorited(articleFavoriteReadService.isUserfavorite(user.getId(),id));
        articleData.setFavoritesCount(articleFavoriteReadService.articleFavoriteCount(id));

    }

    @Override
    public void setFavoriteCount(List<ArticleData> articles) {
        List<ArticleFavoriteCount> favoriteCounts = articleFavoriteReadService.articlesFavoriteCount(
                articles.stream().map(ArticleData::getId).collect(toList()));
        Map<String,Integer> countMap=new HashMap<>();
        favoriteCounts.forEach(
                item->{
                    countMap.put(item.getId(), item.getCount());
                }
        );
        articles.forEach(
                articleData -> articleData.setFavoritesCount(countMap.get(articleData.getId())));
        /*for (ArticleData articleData : articles) {
            articleData.setFavoritesCount(countMap.get(articleData.getId()));
        }*/
    }

    @Override
    public void setIsfavorite(List<ArticleData> articles, User currentUser) {
        Set<String> favoritedArticles = articleFavoriteReadService.userFavorites(
                articles.stream().map(ArticleData::getId).collect(toList()),
                currentUser);
        articles.forEach(
                articleData -> {
                    if (favoritedArticles.contains(articleData.getId())) {
                        articleData.setFavorited(true);
                    }
                });
        /*for (ArticleData articleData : articles) {
            if (favoritedArticles.contains(articleData.getId())) {
                articleData.setFavorited(true);
            }
        }*/

    }
}
