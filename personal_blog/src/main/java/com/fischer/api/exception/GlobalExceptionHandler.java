package com.fischer.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        Map<String,Object> map=new HashMap<>();
        map.put("message",e.getMessage());
        map.put("code",e.getStatus().value());
        logger.error("发生业务异常，具体情况为："+e.getMessage());
        return new ResponseEntity(map,e.getStatus());
    }

    //@ExceptionHandler(NullPointerException.class)
    public ResponseEntity nullPointerHandler(HttpServletRequest req,NullPointerException e){
        Map<String,Object> map=new HashMap<>();
        map.put("message","服务器出现异常，可能数据已不存在，请联系管理员");
        map.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("发生空指针异常");
        return new ResponseEntity(map, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity NotVaildExceptionHandler(HttpServletRequest req,MethodArgumentNotValidException e){
        Map<String,Object> map=new HashMap<>();
        List<ObjectError> allErrors = e.getAllErrors();
        for(ObjectError objectError:allErrors){
            map.put("message",objectError.getDefaultMessage());
            logger.error("表单数据校验未通过，原因为:"+objectError.getDefaultMessage());
        }
        map.put("code",HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(map,HttpStatus.BAD_REQUEST);

    }

    //@ExceptionHandler(Exception.class)
    public ResponseEntity unknowErrorException(HttpServletRequest req,Exception e){
        Map<String,Object> map=new HashMap<>();
        map.put("message","出现未知异常!请联系管理员");
        map.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("出现未知异常，详细信息:"+e.getMessage());
        return new ResponseEntity(map,HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity updateViolation(HttpServletRequest req,ConstraintViolationException e){
        Map<String,Object> map=new HashMap<>();
        String temp=e.getMessage();
        String sub=null;
        for(int i=0;i<temp.length();i++){
            if(temp.charAt(i)==' ');{
                sub=temp.substring(i+1);
                break;
            }
        }
        map.put("message",sub);
        map.put("code",HttpStatus.BAD_REQUEST.value());
        logger.error("出现未知异常，详细信息:"+temp);
        return new ResponseEntity(map,HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
