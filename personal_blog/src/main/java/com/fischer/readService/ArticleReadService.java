package com.fischer.readService;

import com.fischer.assistant.MyPage;
import com.fischer.data.ArticleData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleReadService {
    ArticleData findById(String id);
    ArticleData findBySlug(String slug);
    List<ArticleData> findArticles(List<String> articelIds);

    List<ArticleData> finArticlesFuzzy(String value, MyPage myPage);


}
