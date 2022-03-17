package com.fischer.readService.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.CommentDao;
import com.fischer.dao.UserDao;
import com.fischer.data.CommentData;
import com.fischer.pojo.Comment;
import com.fischer.pojo.User;
import com.fischer.readService.CommentReadService;
import com.fischer.repository.ArticleRepository;
import com.fischer.repository.CommentRepository;
import com.fischer.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class CommentReadServiceImpl implements CommentReadService {


    private CommentDao commentDao;
    private UserDao userDao;
    @Autowired
    public CommentReadServiceImpl (CommentDao commentDao, UserDao userDao)
    {
        this.commentDao=commentDao;
        this.userDao=userDao;
    }
    @Override
    public CommentData findById(String id) {
        LambdaQueryWrapper<Comment> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(id),Comment::getId,id);
        Comment comment = commentDao.selectOne(lqw);
        User user = userDao.selectById(comment.getUserId());
        return new CommentData(comment,user);

    }
    @Override
    public List<CommentData> findByArticleId(String articleId) {
        LambdaQueryWrapper<Comment> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(articleId),Comment::getArticleId,articleId);
        List<CommentData> commentDataList=new LinkedList<>();
        for (Comment comment : commentDao.selectList(lqw)) {
            User user = userDao.selectById(comment.getUserId());
            CommentData commentData=new CommentData(comment,user);
            commentDataList.add(commentData);
        }

        return commentDataList;
    }
}
