package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "article_tags")
public class ArticleTagRelation {
    private String articleId;
    private String tagId;

}
