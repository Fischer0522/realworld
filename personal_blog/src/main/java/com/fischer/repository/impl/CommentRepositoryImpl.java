package com.fischer.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.CommentDao;
import com.fischer.pojo.Comment;
import com.fischer.repository.CommentRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    @Autowired
    private CommentDao commentDao;
    @Override
    public void save(Comment comment) {
        commentDao.insert(comment);

    }

    @Override
    public Optional<Comment> find(String articleId, String id) {
        LambdaQueryWrapper<Comment> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(articleId),Comment::getArticleId,articleId);
        lqw.eq(Strings.isNotEmpty(id),Comment::getId,id);
        Comment comment = commentDao.selectOne(lqw);
        return Optional.ofNullable(comment);
    }

    @Override
    public void remove(Comment comment) {
        if (find(comment.getArticleId(),comment.getId()).isPresent()) {
            LambdaQueryWrapper<Comment> lqw=new LambdaQueryWrapper<>();
            lqw.eq(Strings.isNotEmpty(comment.getArticleId()),Comment::getArticleId,comment.getArticleId());
            lqw.eq(Strings.isNotEmpty(comment.getId()),Comment::getId,comment.getId());
            commentDao.delete(lqw);
        }

    }
}
