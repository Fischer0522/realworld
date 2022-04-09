package com.fischer.api;

import com.fischer.assistant.MyPage;
import com.fischer.assistant.ResultType;
import com.fischer.assistant.Util;
import com.fischer.data.ArticleDataList;
import com.fischer.data.NewArticleParam;
import com.fischer.jwt.JwtService;
import com.fischer.pojo.Article;
import com.fischer.pojo.User;
import com.fischer.service.article.ArticleCommandService;
import com.fischer.service.article.ArticleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;

@Validated
@RestController
@RequestMapping("/articles")
public class ArticlesApi {
    private ArticleQueryService articleQueryService;
    private ArticleCommandService articleCommandService;
    private JwtService jwtService;
    @Autowired
    public ArticlesApi
            (ArticleQueryService articleQueryService,
             ArticleCommandService articleCommandService,
             JwtService jwtService) {
        this.articleCommandService=articleCommandService;
        this.articleQueryService=articleQueryService;
        this.jwtService=jwtService;
    }
    @PostMapping
    public ResponseEntity createArticle(
            @Valid @RequestBody NewArticleParam newArticleParam,
            @RequestHeader(value = "Authorization")String token){
        User user = jwtService.toUser(token).get();
        Article article = articleCommandService.createArticle(newArticleParam, user);
        /*return ResponseEntity.ok(
                new HashMap<String,Object>() {
                    {
                    put("article",articleQueryService.findById(article.getId(),user).get());
                }
                });*/
        return ResponseEntity.status(200).body(new ResultType(HttpStatus.CREATED.value(),
                articleQueryService.findById(article.getId(),user).get(),
                "创建文章成功"));
    }
    @GetMapping(path = "exact")
    public ResponseEntity getArticles(
            @RequestParam(value = "offset",defaultValue = "0") int offset,
            @RequestParam(value = "limit",defaultValue = "20") int limit,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "favorited",required = false) String favoritedBy,
            @RequestParam(value = "author",required = false) String author,
            @RequestHeader(value = "Authorization",required = false)String token) {
        User user;
        if(!Util.isEmpty(token)) {
            user = jwtService.toUser(token).get();
        }
        else {
            user=null;
        }
        ArticleDataList recentArticles = articleQueryService.findRecentArticles(
                tag,
                author,
                favoritedBy,
                new MyPage(offset, limit),
                user);
        /*return ResponseEntity.ok(recentArticles);*/
        if(recentArticles.getCount()==0){
            return ResponseEntity.ok(new ResultType(HttpStatus.OK.value(), recentArticles,"查询结果为空"));
        }
        return ResponseEntity.ok(new ResultType(HttpStatus.OK.value(), recentArticles,"查询成功"));
    }


    @GetMapping(path="fuzzy")
    public ResponseEntity getArticlesFuzzy(
            @RequestParam(value = "offset",defaultValue ="0") int offset,
            @RequestParam(value = "limit",defaultValue = "5") int limit,
            @RequestParam(value = "value",required = true) String value,
            @RequestHeader(value = "Authorization",required = false) String token
           ){
        User user;
        if(!Util.isEmpty(token)) {
            user = jwtService.toUser(token).get();
        }
        else {
            user=null;
        }
        ArticleDataList articleDataList = articleQueryService.findRecentArticlesFuzzy(
                value,
                new MyPage(offset, limit),
                user
        );
        if(articleDataList.getCount()==0){
            return ResponseEntity.ok(new ResultType(HttpStatus.OK.value(), articleDataList,"查询结果为空"));
        }
        return ResponseEntity.ok(new ResultType(HttpStatus.OK.value(), articleDataList,"ok"));

    }


}
