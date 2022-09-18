package com.fischer.repository;

import com.fischer.pojo.ArticleFavorite;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleFavoriteRepository {
    void save(ArticleFavorite articleFavorite);

    Optional<ArticleFavorite> find(String articleId,String userId);

    void remove(ArticleFavorite favorite);
}
