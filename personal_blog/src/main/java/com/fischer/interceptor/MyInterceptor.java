package com.fischer.interceptor;

import com.fischer.api.exception.BizException;
import com.fischer.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class MyInterceptor implements HandlerInterceptor {
    @Autowired
    JwtService jwtService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if(token==null){
            throw new BizException(HttpStatus.UNAUTHORIZED,"未携带Token");
        }
        Optional<String> subFromToken = jwtService.getSubFromToken(token);
        if(!subFromToken.isPresent()){
            throw new BizException(HttpStatus.UNAUTHORIZED,"token解析失败,请联系管理员");
        }
        String id="loginUser:"+subFromToken.get();
        String s = redisTemplate.opsForValue().get(id);
        if(s==null){
            throw new BizException(HttpStatus.UNAUTHORIZED,"未登录！请先登录");
        }
        return true;


    }
}
