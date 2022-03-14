package com.fischer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.assistant.MyPage;
import com.fischer.pojo.Article;
import com.fischer.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleDao extends BaseMapper<Article> {
    Integer countArticle(@Param("tag") String tag,
                         @Param("author") String author,
                         @Param("favoritedBy") String favoritedBy);
    List<String> queryArticles(
            @Param("tag") String tag,
            @Param("author") String author,
            @Param("favoritedBy") String favoritedBy,
            @Param("page") MyPage myPage
            );


}
