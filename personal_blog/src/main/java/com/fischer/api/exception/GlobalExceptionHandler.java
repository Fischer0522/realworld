package com.fischer.api.exception;

import com.fischer.assistant.ResultType;
import com.fischer.dao.UserDao;
import com.fischer.jwt.JwtService;
import com.fischer.pojo.Log;
import com.fischer.pojo.User;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private JwtService jwtService;
    private MongoTemplate mongoTemplate;
    private UserDao userDao;
    public GlobalExceptionHandler(
            JwtService jwtService,
            MongoTemplate mongoTemplate,
            UserDao userDao){
        this.jwtService=jwtService;
        this.mongoTemplate=mongoTemplate;
        this.userDao=userDao;
    }
    private static final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public ResponseEntity bizExceptionHandler(HttpServletRequest req,BizException e){
        ResultType resultType=new ResultType(e.getStatus().value(),null,e.getMessage());
        logger.error("发生业务异常，具体情况为："+e.getMessage());
        e.printStackTrace();
        collectException(req, e.getStackTrace(), e.getClass().getName(),e.getStatus().value(),e.getMessage());
        return new ResponseEntity(resultType,HttpStatus.OK);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity nullPointerHandler(HttpServletRequest req,NullPointerException e){
        ResultType resultType=new ResultType( HttpStatus.INTERNAL_SERVER_ERROR.value(),null,"服务器出现空指针异常，可能数据已不存在，请联系管理员");
        logger.error(e.getMessage());
        e.printStackTrace();
        collectException(req, e.getStackTrace(), e.getClass().getName(),500,"服务器出现空指针异常，可能数据已不存在，请联系管理员");
        return new ResponseEntity(resultType, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity NotVaildExceptionHandler(HttpServletRequest req,MethodArgumentNotValidException e){

        List<ObjectError> allErrors = e.getAllErrors();
        String temp="";
        for(ObjectError objectError:allErrors){

            temp=objectError.getDefaultMessage();
            logger.error("表单数据校验未通过，原因为:"+objectError.getDefaultMessage());
            collectException(req, e.getStackTrace(), e.getClass().getName(),400,objectError.getDefaultMessage());
        }
        ResultType resultType=new ResultType(HttpStatus.BAD_REQUEST.value(), null,temp);
        e.printStackTrace();

        return new ResponseEntity(resultType,HttpStatus.OK);

    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity sizeLimit(HttpServletRequest req,MaxUploadSizeExceededException e){
        logger.error(e.getMessage());
        ResultType resultType=new ResultType(HttpStatus.BAD_REQUEST.value(), null,"上传的文件超出限制，请上传3m以下的文件");
        e.printStackTrace();
        collectException(req, e.getStackTrace(), e.getClass().getName(),400,"上传的文件超出限制，请上传3MB一下的图片");
        return new ResponseEntity(resultType,HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity unknowErrorException(HttpServletRequest req,Exception e){
        logger.error("出现未知异常，详细信息:"+e.getMessage());
        ResultType resultType=new ResultType(HttpStatus.INTERNAL_SERVER_ERROR.value(), null,"出现未知异常!请联系管理员,详情:"+e.getMessage());
        e.printStackTrace();
        collectException(req, e.getStackTrace(), e.getClass().getName(),500,"出现未知异常！请联系管理员，详情："+e.getMessage());
        return new ResponseEntity(resultType,HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity updateViolation(HttpServletRequest req,ConstraintViolationException e){
        String temp=e.getMessage();
        String sub=null;
        for(int i=0;i<temp.length()-1;i++){

            String su=temp.substring(i,i+1);
            if(su.equals(":")){
                sub=temp.substring(i+2);
            }
        }
        ResultType resultType=new ResultType(HttpStatus.BAD_REQUEST.value(), null,sub);
        e.printStackTrace();
        collectException(req, e.getStackTrace(), e.getClass().getName(),400,sub);
        return new ResponseEntity(resultType,HttpStatus.OK);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity emailOrPasswordError(HttpServletRequest req,BadCredentialsException e){
        logger.error(e.getMessage());
        ResultType resultType=new ResultType(HttpStatus.UNAUTHORIZED.value(), null,"邮箱或密码错误");
        e.printStackTrace();
        collectException(req, e.getStackTrace(), e.getClass().getName(),401,"邮箱或密码错误");
        return new ResponseEntity(resultType,HttpStatus.OK);
    }

    void collectException(
            HttpServletRequest request,
            StackTraceElement[] stackTrace,
            String className,
            Integer status,
            String message){
        String token = request.getHeader("Authorization");
        Optional<String> subFromToken = jwtService.getSubFromToken(token);
        String username="匿名访问";
        if(subFromToken.isPresent()){
             username = userDao.selectById(subFromToken.get()).getUsername();
        }
        List<String> collect = Arrays
                .stream(stackTrace)
                .map(stackTraceElement -> stackTraceElement.toString())
                .collect(Collectors.toList());

        Log log=new Log(
            className,
                LocalDateTime.now().toString(),
                username,
                collect,
                status,message
        );
        mongoTemplate.save(log);

    }



}
