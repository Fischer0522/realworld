package com.fischer.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.pojo.Comment;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestCommentDao {
    @Autowired
    private CommentDao commentDao;
    @Test
    public void  testFindById()
    {
        String id="a3db9ed9-7cb7-4fe2-8206-1ce5854dc938";
        LambdaQueryWrapper<Comment> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(id),Comment::getId,id);
        Comment comment = commentDao.selectOne(lqw);
        System.out.println(comment);
    }
}
