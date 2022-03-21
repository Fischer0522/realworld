package com.fischer.readService.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fischer.api.exception.BizException;
import com.fischer.assistant.MyPage;
import com.fischer.dao.ImageDao;
import com.fischer.data.ArticleData;
import com.fischer.pojo.Article;
import com.fischer.pojo.User;
import com.fischer.readService.ArticleReadService;
import com.fischer.repository.ArticleRepository;
import com.fischer.repository.UserRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleReadServiceImpl implements ArticleReadService {

    private ArticleRepository articleRepository;

    private UserRepository userRepository;
    private ImageDao imageDao;
    @Autowired
    public ArticleReadServiceImpl(
            ArticleRepository articleRepository,
            UserRepository userRepository,
            ImageDao imageDao){
        this.imageDao=imageDao;
        this.articleRepository=articleRepository;
        this.userRepository=userRepository;
    }
    @Override
    public ArticleData findById(String id) {
        Optional<Article> article = articleRepository.findById(id);
        if (!article.isPresent()){
            return null;
        }
        Optional<User> user = userRepository.findById(article.get().getUserId());
        if(!user.isPresent()){
            return null;
        }
        return new ArticleData(article.get(),user.get());
    }

    @Override
    public ArticleData findBySlug(String slug) {
        Optional<Article> article = articleRepository.findBySlug(slug);
        if (!article.isPresent()){
            return null;
        }
        Optional<User> user = userRepository.findById(article.get().getUserId());
        if(!user.isPresent()){
            return null;
        }
        return new ArticleData(article.get(),user.get());
    }

    @Override
    public List<ArticleData> findArticles(List<String> articelIds) {
        List<Article> articles = articleRepository
                .findArticles(articelIds)
                .orElseThrow(() -> new BizException(HttpStatus.NOT_FOUND, "文章不存在"));

        List<ArticleData> articleData=new LinkedList<>();

        for(Article article:articles)
        {
            User user = userRepository
                    .findById(article.getUserId())
                    .orElseThrow(() -> new BizException(HttpStatus.NOT_FOUND, "数据出现错误！当前文章无匹配作者"));

            ArticleData AD=new ArticleData(article,user);
            articleData.add(AD);
        }
        return articleData;
    }

    @Override
    public List<ArticleData> finArticlesFuzzy(String title, String description,MyPage myPage) {
        Article article=new Article();
        article.setTitle(title);
        article.setDescription(description);
        IPage<Article> page = articleRepository.getPage(myPage, article);
        List<Article> articles = page.getRecords();
        List<ArticleData> articleData=new LinkedList<>();


        for(Article art:articles)
        {
            User user = userRepository
                    .findById(art.getUserId())
                    .orElseThrow(()->new BizException(HttpStatus.NOT_FOUND,"数据出现错误！当前文章无匹配作者"));

            ArticleData AD=new ArticleData(art,user);
            articleData.add(AD);
        }
        return articleData;
    }
}
