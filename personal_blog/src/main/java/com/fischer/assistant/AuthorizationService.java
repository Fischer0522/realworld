package com.fischer.assistant;

import com.fischer.dao.AdminDao;
import com.fischer.pojo.Admin;
import com.fischer.pojo.Article;
import com.fischer.pojo.Comment;
import com.fischer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class AuthorizationService {
    @Autowired
    private  AdminDao adminDao;
    public  boolean canWriterArticle(User user, Article article){
        Admin admin = adminDao.selectById(user.getId());
        if (admin!=null){
            if(admin.getId().equals(user.getId())){
                return true;
            }
        }
        return user.getId().equals(article.getUserId());
    }

    public  boolean canWriteComment(User user, Article article, Comment comment){
        Admin admin = adminDao.selectById(user.getId());
        if (admin!=null){
            if(admin.getId().equals(user.getId())){
            return true;
            }
        }
        return user.getId().equals(article.getUserId())||user.getId().equals(comment.getUserId());

    }
}
