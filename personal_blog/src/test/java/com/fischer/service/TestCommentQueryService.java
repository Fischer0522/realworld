package com.fischer.service;

import com.fischer.dao.ArticleDao;
import com.fischer.dao.UserDao;
import com.fischer.data.CommentData;
import com.fischer.pojo.User;
import com.fischer.service.comment.CommentQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TestCommentQueryService {
    @Autowired
    private CommentQueryService commentQueryService;
    @Autowired
    private UserDao userDao;
    @Test
    public void testFindById(){
        String id="a3db9ed9-7cb7-4fe2-8206-1ce5854dc938";
        Optional<CommentData> commentData = commentQueryService.findById(id);
        System.out.println(commentData.get());
    }
    @Test
    public void testFindByArticleId(){
        User user = userDao.selectById("1234");
        String articleId="e30e99ae-5bdf-4a3f-90fa-49cd18692eac";
        List<CommentData> commentData = commentQueryService.findByArticleId(articleId,user);
        System.out.println(commentData);
    }

}
