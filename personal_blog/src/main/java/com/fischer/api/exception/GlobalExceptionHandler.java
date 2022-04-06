package com.fischer.api.exception;

import com.fischer.assistant.ResultType;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public ResponseEntity bizExceptionHandler(HttpServletRequest req,BizException e){
        ResultType resultType=new ResultType(e.getStatus().value(),null,e.getMessage());
        logger.error("发生业务异常，具体情况为："+e.getMessage());
        e.printStackTrace();
        return new ResponseEntity(resultType,HttpStatus.OK);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity nullPointerHandler(HttpServletRequest req,NullPointerException e){
        ResultType resultType=new ResultType( HttpStatus.INTERNAL_SERVER_ERROR.value(),null,"服务器出现异常，可能数据已不存在，请联系管理员");
        logger.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity(resultType, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity NotVaildExceptionHandler(HttpServletRequest req,MethodArgumentNotValidException e){

        List<ObjectError> allErrors = e.getAllErrors();
        String temp="";
        for(ObjectError objectError:allErrors){

            temp=objectError.getDefaultMessage();
            logger.error("表单数据校验未通过，原因为:"+objectError.getDefaultMessage());
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
        return new ResponseEntity(resultType,HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity unknowErrorException(HttpServletRequest req,Exception e){
        logger.error("出现未知异常，详细信息:"+e.getMessage());
        ResultType resultType=new ResultType(HttpStatus.INTERNAL_SERVER_ERROR.value(), null,"出现未知异常!请联系管理员,详情:"+e.getMessage());
        e.printStackTrace();
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
        logger.error("出现未知异常，详细信息:"+sub);
        e.printStackTrace();
        return new ResponseEntity(resultType,HttpStatus.OK);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity emailOrPasswordError(HttpServletRequest req,BadCredentialsException e){
        logger.error(e.getMessage());
        ResultType resultType=new ResultType(HttpStatus.UNAUTHORIZED.value(), null,"用户名或密码错误");
        e.printStackTrace();
        return new ResponseEntity(resultType,HttpStatus.OK);
    }



}
