package com.fischer.service.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fischer.assistant.MyPage;
import com.fischer.data.ArticleData;
import com.fischer.data.ArticleDataList;
import com.fischer.data.NewArticleParam;
import com.fischer.pojo.Article;
import com.fischer.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ArticleQueryService {
     Optional<ArticleData> findById(String id, User user);

     Optional<ArticleData> findBySlug(String slug,User user);

     ArticleDataList findRecentArticles(
             String tag,
             String author,
             String favoritedBy,
             MyPage myPage,
             User currentUser);

     ArticleDataList findRecentArticlesFuzzy(
             String value,
             MyPage myPage,
             User currentUser
     );

     void fillExtraInfo(List<ArticleData> articles,User currentUser);

     void fillExtreInfo(String id,User user,ArticleData articleData);

     void setFavoriteCount(List<ArticleData> articles);

     void setIsfavorite(List<ArticleData> articles,User currentUser);




}
