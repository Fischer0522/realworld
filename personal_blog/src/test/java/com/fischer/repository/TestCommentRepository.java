package com.fischer.repository;

import com.fischer.api.exception.BizException;
import com.fischer.pojo.Comment;
import com.fischer.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@SpringBootTest
public class TestCommentRepository {
    @Autowired
    private CommentRepository commentRepository;
    @Test
    public void testSave()
    {
        String body="收到收到收到";
        String articleId="e30e99ae-5bdf-4a3f-90fa-49cd18692eac";
        String userId="1234";
        Comment comment=new Comment(
                body,
                userId,
                articleId
        );
        commentRepository.save(comment);
    }

    @Test
    public void testRemove(){
        String articleId="e30e99ae-5bdf-4a3f-90fa-49cd18692eac";
        String id="33a3c2b7-0c1e-45ce-98b6-e4aa855f85ce";
        Optional<Comment> comment = commentRepository.find(articleId, id);
        commentRepository.remove(comment.get());
    }
}
