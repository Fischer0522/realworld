package com.fischer.service.comment;

import com.fischer.data.CommentData;
import com.fischer.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CommentQueryService {
    Optional<CommentData> findById(String id);

    List<CommentData> findByArticleId(String articleId, User user);
}
