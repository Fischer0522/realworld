package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@TableName(value = "article_favorites")
public class ArticleFavorite {
    private String articleId;
    private String userId;

    public ArticleFavorite(String articleId, String userId)
    {
        this.articleId=articleId;
        this.userId=userId;
    }

}
