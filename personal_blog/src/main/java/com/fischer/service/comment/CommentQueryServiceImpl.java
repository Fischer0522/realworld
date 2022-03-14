package com.fischer.service.comment;

import com.fischer.data.CommentData;
import com.fischer.pojo.User;
import com.fischer.readService.CommentReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CommentQueryServiceImpl implements CommentQueryService{
    @Autowired
    private CommentReadService commentReadService;
    @Override
    public Optional<CommentData> findById(String id) {
        CommentData commentData = commentReadService.findById(id);
        if(commentData==null){
            return Optional.empty();
        }
        /*关注相关的功能日后再做*/
        else{
          return Optional.ofNullable(commentData);
        }
    }

    @Override
    public List<CommentData> findByArticleId(String articleId, User user) {
        List<CommentData> commentDataList = commentReadService.findByArticleId(articleId);
        /*关注相关的功能日后再做*/
        return commentDataList;
    }
}
