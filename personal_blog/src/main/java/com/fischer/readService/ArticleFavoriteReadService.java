package com.fischer.readService;

import com.fischer.data.ArticleFavoriteCount;
import com.fischer.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ArticleFavoriteReadService {
    boolean isUserfavorite(String userId,String articleId);

    int articleFavoriteCount(String articleId);

    List<ArticleFavoriteCount> articlesFavoriteCount(List<String> ids);

    Set<String> userFavorites(List<String> ids, User currentUser);


}
