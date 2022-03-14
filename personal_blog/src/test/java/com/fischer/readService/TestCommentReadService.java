package com.fischer.readService;

import com.fischer.data.CommentData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestCommentReadService {
    @Autowired
    private CommentReadService commentReadService;
    @Test
    public  void testFindById(){
        String id="a3db9ed9-7cb7-4fe2-8206-1ce5854dc938";
        CommentData commentData = commentReadService.findById(id);
        System.out.println(commentData);

    }
    @Test
    public void testFindByArticleId(){
        String articleId="e30e99ae-5bdf-4a3f-90fa-49cd18692eac";
        for (CommentData commentData : commentReadService.findByArticleId(articleId)) {
            System.out.println(commentData);
        }

    }
}
