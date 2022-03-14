package com.fischer.repository;

import com.fischer.pojo.Comment;

import java.util.Optional;

public interface CommentRepository {
    void save(Comment comment);

    Optional<Comment> find(String articleId,String id);

    void remove(Comment comment);
}
