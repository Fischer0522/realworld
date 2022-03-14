package com.fischer.assistant;

import com.fischer.pojo.Article;
import com.fischer.pojo.Comment;
import com.fischer.pojo.User;

public class AuthorizationService {
    public static boolean canWriterArticle(User user, Article article){
        return user.getId().equals(article.getUserId());
    }

    public static boolean canWriteComment(User user, Article article, Comment comment){
        return user.getId().equals(article.getUserId())||user.getId().equals(comment.getUserId());

    }
}
