package com.fischer.api;

import com.fischer.api.exception.BizException;
import com.fischer.assistant.AuthorizationService;
import com.fischer.data.ArticleData;
import com.fischer.data.UpdateArticleParam;
import com.fischer.jwt.JwtService;
import com.fischer.pojo.Article;
import com.fischer.pojo.User;
import com.fischer.repository.ArticleRepository;
import com.fischer.service.article.ArticleCommandService;
import com.fischer.service.article.ArticleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*对单篇文章进行CRUD*/
@RestController
@RequestMapping(path = "/articles/{slug}")
public class ArticleApi {
    private ArticleQueryService articleQueryService;
    private ArticleRepository articleRepository;
    private ArticleCommandService articleCommandService;
    private JwtService jwtService;
    private AuthorizationService authorizationService;

    @Autowired
    public ArticleApi(
            ArticleQueryService articleQueryService,
            ArticleRepository articleRepository,
            ArticleCommandService articleCommandService,
            JwtService jwtService,
            AuthorizationService authorizationService) {
        this.articleQueryService=articleQueryService;
        this.articleCommandService=articleCommandService;
        this.articleRepository=articleRepository;
        this.jwtService=jwtService;
        this.authorizationService=authorizationService;
    }
    @GetMapping
    public ResponseEntity<?>article(
            @PathVariable("slug") String slug,
            @RequestHeader(value ="token")String token){
        User user = jwtService.toUser(token).get();
        Optional<ArticleData> articleData = articleQueryService.findBySlug(slug, user);
            return articleData
                    .map(articleData1 -> ResponseEntity.ok(articleResponse(articleData1)))
                    .orElseThrow(()->new BizException(HttpStatus.NOT_FOUND,"资源请求失败，未找到该文章"));

    }

    @PutMapping
    public ResponseEntity<?> updateArticle(
            @PathVariable("slug") String slug,
           @RequestHeader(value = "token") String token,
            @Valid @RequestBody UpdateArticleParam updateArticleParam){
        User user = jwtService.toUser(token).get();
        return articleRepository
                .findBySlug(slug)
                .map(article -> {
                    if(!authorizationService.canWriterArticle(user,article)){
                        throw  new BizException(HttpStatus.FORBIDDEN,"非常抱歉，您没有权限对该文章进行修改");
                    }
                    else{
                        Article updateArticle = articleCommandService.updateArticle(article, updateArticleParam);
                        return ResponseEntity.ok(
                                articleResponse(articleQueryService.findBySlug(updateArticle.getSlug(),user).get()));

                        }
                })
                .orElseThrow(()->new BizException(HttpStatus.NOT_FOUND,"资源请求失败，要更新的文章可能已经不存在"));

    }

    @DeleteMapping
    public ResponseEntity deleteArticle(
            @PathVariable("slug") String slug,
            @RequestHeader(value = "token")String token){
        User user = jwtService.toUser(token).get();
        return articleRepository.findBySlug(slug)
                .map(article -> {
                    if(!authorizationService.canWriterArticle(user,article)){
                        throw new BizException(HttpStatus.FORBIDDEN,"非常抱歉，您无法删除该文章");
                    }
                    else{
                        articleRepository.remove(article);
                        return ResponseEntity.noContent().build();
                    }
                })
                .orElseThrow(()->new BizException(HttpStatus.NOT_FOUND,"资源请求失败，要删除的文章已经不存在"));
    }
    private Map<String,Object> articleResponse(ArticleData articleData){
        return new HashMap<String,Object>(){
            {
                put("article",articleData);
            }
        };
    }



}
