package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fischer.assistant.TimeCursor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

import java.util.UUID;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@TableName(value = "comments")
@ToString
public class Comment {
    private String id;
    private String body;
    private String userId;
    private String articleId;
    private String createdAt;

    public Comment(String body,String userId,String articleId)
    {   this.id= UUID.randomUUID().toString();
        this.body=body;
        this.userId=userId;
        this.articleId=articleId;
        this.createdAt= TimeCursor.toTime(DateTime.now());
    }
}
