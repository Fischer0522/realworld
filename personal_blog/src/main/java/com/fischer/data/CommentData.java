package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fischer.pojo.Comment;
import com.fischer.pojo.User;
import lombok.Data;
import org.joda.time.DateTime;
@Data
public class CommentData {
    private String id;
    private String body;
    @JsonIgnore
    private String articleId;
    private String createdAt;

    @JsonProperty
    private ProfileData profileData;

    public CommentData(Comment comment, User user)
    {
        this.id=comment.getId();
        this.body=comment.getBody();
        this.articleId=comment.getArticleId();
        this.createdAt=comment.getCreatedAt();
        this.profileData=new ProfileData(user);
    }

}
