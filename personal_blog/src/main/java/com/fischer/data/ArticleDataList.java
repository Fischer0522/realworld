package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.List;

@ToString
public class ArticleDataList {
    @JsonProperty("articles")
    private final List<ArticleData> articleDatas;
    @JsonProperty("articlesCount")
    private final int count;
    public ArticleDataList(List<ArticleData> articleDatas,int count) {
        this.articleDatas=articleDatas;
        this.count=count;
    }

}
