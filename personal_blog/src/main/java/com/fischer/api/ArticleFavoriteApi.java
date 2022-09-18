package com.fischer.api;

import com.fischer.api.exception.BizException;
import com.fischer.assistant.ResultType;
import com.fischer.dao.UserDao;
import com.fischer.data.ArticleData;
import com.fischer.jwt.JwtService;
import com.fischer.pojo.Article;
import com.fischer.pojo.ArticleFavorite;
import com.fischer.pojo.User;
import com.fischer.repository.ArticleFavoriteRepository;
import com.fischer.repository.ArticleRepository;
import com.fischer.service.article.ArticleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(path = "articles/{slug}/favorite")
public class ArticleFavoriteApi {
    private ArticleFavoriteRepository articleFavoriteRepository;
    private ArticleRepository articleRepository;
    private ArticleQueryService articleQueryService;
    private JwtService jwtService;
    @Autowired
    public ArticleFavoriteApi(
            ArticleRepository articleRepository,
            ArticleFavoriteRepository articleFavoriteRepository,
            ArticleQueryService articleQueryService,
            JwtService jwtService
    ){
        this.articleFavoriteRepository=articleFavoriteRepository;
        this.articleRepository=articleRepository;
        this.articleQueryService=articleQueryService;
        this.jwtService=jwtService;

    }
    @PostMapping
    public ResponseEntity favoriteArticle(
            @PathVariable("slug") String slug,
            @RequestHeader(value = "Authorization")String token){
        User user = jwtService.toUser(token).get();
        Article article = articleRepository.findBySlug(slug).orElseThrow(()-> new BizException(HttpStatus.NOT_FOUND,"资源请求失败，要点赞的文章已经不存在"));

        ArticleFavorite articleFavorite=new ArticleFavorite(article.getId(),user.getId());
        articleFavoriteRepository.save(articleFavorite);
        Optional<ArticleData> articleData = articleQueryService.findBySlug(slug,user);
        return responseArticleData(articleData.get());

    }
    @DeleteMapping
    public ResponseEntity upfavoriteArticle(
            @PathVariable("slug") String slug,
            @RequestHeader(value = "Authorization")String token){
        User user = jwtService.toUser(token).get();
        Article article = articleRepository.findBySlug(slug).orElseThrow(()-> new BizException(HttpStatus.NOT_FOUND,"资源请求失败，该文章可能已经被删除"));
        Optional<ArticleFavorite> articleFavorite = articleFavoriteRepository.find(article.getId(), user.getId());
        articleFavorite.ifPresent(favorite -> articleFavoriteRepository.remove(favorite));
        Optional<ArticleData> articleData = articleQueryService.findBySlug(slug, user);
        return ResponseEntity.ok(new ResultType(204,articleData,"ok"));
    }


    private ResponseEntity<ResultType> responseArticleData(
            final ArticleData articleData){
        return ResponseEntity.ok(new ResultType(HttpStatus.OK.value(),articleData,"favorite成功"));
    }


}

