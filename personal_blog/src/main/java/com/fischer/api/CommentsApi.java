package com.fischer.api;

import com.fischer.api.exception.BizException;
import com.fischer.assistant.AuthorizationService;
import com.fischer.assistant.ResultType;
import com.fischer.assistant.Util;
import com.fischer.data.CommentData;
import com.fischer.data.NewCommentParam;
import com.fischer.jwt.JwtService;
import com.fischer.pojo.Article;
import com.fischer.pojo.Comment;
import com.fischer.pojo.User;
import com.fischer.repository.ArticleRepository;
import com.fischer.repository.CommentRepository;
import com.fischer.service.comment.CommentQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping(path = "/articles/{slug}/comments")
public class CommentsApi {
    private ArticleRepository articleRepository;
    private CommentRepository commentRepository;
    private CommentQueryService commentQueryService;
    private JwtService jwtService;
    private AuthorizationService authorizationService;
    @Autowired
    public CommentsApi(
            ArticleRepository articleRepository,
            CommentRepository commentRepository,
            CommentQueryService commentQueryService,
            JwtService jwtService,
            AuthorizationService authorizationService){
        this.articleRepository=articleRepository;
        this.commentQueryService=commentQueryService;
        this.commentRepository=commentRepository;
        this.jwtService=jwtService;
        this.authorizationService=authorizationService;
    }
    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable("slug") String slug,
            @RequestHeader(value = "Authorization") String token,
            @Valid  @RequestBody NewCommentParam newCommentParam){
        User user = jwtService.toUser(token).get();
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(()-> new BizException(HttpStatus.NOT_FOUND,"资源请求失败，该文章已经不存在"));
        Comment comment=new Comment(newCommentParam.getBody(),user.getId() ,article.getId());
        commentRepository.save(comment);
        Optional<CommentData> commentData = commentQueryService.findById(comment.getId());
        return ResponseEntity.status(201).body(commentResponse(commentData.get()));
    }
    @GetMapping
    public ResponseEntity getComments(
            @PathVariable("slug") String slug,
            @RequestHeader(value = "Authorization",required = false)String token){
        User user;
        if(!Util.isEmpty(token)) {
            user = jwtService.toUser(token).get();
        }
        else {
            user=null;
        }
        Article article = articleRepository.findBySlug(slug).orElseThrow(()->new BizException(HttpStatus.NOT_FOUND,"该文章已经不存在"));
        List<CommentData> commentDataList = commentQueryService.findByArticleId(article.getId(), user);
        /*return ResponseEntity.ok(
                new HashMap<String,Object>(){
            {
                put("comments",commentDataList);
            }
        });*/
        if(commentDataList.isEmpty()){
            return ResponseEntity
                    .ok(new ResultType(HttpStatus.OK.value(), commentDataList,"查询结果为空"));
        }
        return ResponseEntity
                .ok(new ResultType(HttpStatus.OK.value(), commentDataList,"查询成功"));
    }
    @DeleteMapping(path = "{id}")
    public ResponseEntity deleteComment(
            @PathVariable("slug") String slug,
            @PathVariable("id") String commentId,
            @RequestHeader(value = "Authorization")String token){
        User user = jwtService.toUser(token).get();
        Article article = articleRepository.findBySlug(slug).orElseThrow(()->new BizException(HttpStatus.NOT_FOUND,"该评论所属的文章已经不存在"));
        return commentRepository.find(article.getId(),commentId )
                .map(comment -> {
                    if(authorizationService.canWriteComment(user,article,comment)){
                        commentRepository.remove(comment);
                        return ResponseEntity.ok(new ResultType(204,null,"删除成功"));
                    }
                        throw new BizException(HttpStatus.FORBIDDEN,"未经授权的操作！");

                }).orElseThrow(()->new BizException(HttpStatus.NOT_FOUND,"该评论已经不存在"));
    }


    private ResultType commentResponse(CommentData commentData){

        /*return new HashMap<String,Object>(){
            {
                put("comment",commentData);
            }
        };*/
        return new ResultType(HttpStatus.CREATED.value(), commentData,"ok");
    }
}
