package com.fischer.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fischer.assistant.MyPage;
import com.fischer.data.ArticleData;
import com.fischer.pojo.Article;
import com.fischer.pojo.Tag;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    void save(Article article);

    Optional<Article> findById(String id);

    Optional<Article> findBySlug(String slug);

    Optional<List<Article>> findArticles(List<String> articleIds);

    void remove(Article article);

    Tag findTag(String tagName);

     IPage<Article> getPage(MyPage myPage, Article article);

}
