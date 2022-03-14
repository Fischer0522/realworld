package com.fischer.readService.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fischer.assistant.MyPage;
import com.fischer.data.ArticleData;
import com.fischer.pojo.Article;
import com.fischer.pojo.User;
import com.fischer.readService.ArticleReadService;
import com.fischer.repository.ArticleRepository;
import com.fischer.repository.UserRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleReadServiceImpl implements ArticleReadService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public ArticleData findById(String id) {
        Optional<Article> article = articleRepository.findById(id);
        Optional<User> user = userRepository.findById(article.get().getUserId());
        ArticleData articleData=new ArticleData(article.get(),user.get());
        return articleData;
    }

    @Override
    public ArticleData findBySlug(String slug) {
        Optional<Article> article = articleRepository.findBySlug(slug);
        Optional<User> user = userRepository.findById(article.get().getUserId());
        ArticleData articleData=new ArticleData(article.get(),user.get());
        return articleData;
    }

    @Override
    public List<ArticleData> findArticles(List<String> articelIds) {
        Optional<List<Article>> temp = articleRepository.findArticles(articelIds);
        List<Article> articles= temp.get();
        List<ArticleData> articleData=new LinkedList<>();

        for(Article article:articles)
        {
            Optional<User> user = userRepository.findById(article.getUserId());
            System.out.println("当前的文章是"+article.getTitle());

            ArticleData AD=new ArticleData(article,user.get());
            ;
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
            Optional<User> user = userRepository.findById(art.getUserId());

            ArticleData AD=new ArticleData(art,user.get());
            ;
            articleData.add(AD);
        }
        return articleData;
    }
}
