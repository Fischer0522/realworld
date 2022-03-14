package com.fischer.readService;

import com.fischer.data.CommentData;

import java.util.List;

public interface CommentReadService {
    CommentData findById(String id);

    List<CommentData> findByArticleId(String articleId);


}
