package com.fischer.filter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.api.exception.BizException;
import com.fischer.dao.UserDao;
import com.fischer.jwt.JwtService;
import com.fischer.pojo.User;
import com.fischer.security.LoginUser;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserDao userDao;
    private StringRedisTemplate redisTemplate;

    @Autowired
    public TokenFilter(
            JwtService jwtService,
            UserDao userDao,
            StringRedisTemplate redisTemplate){
        this.jwtService=jwtService;
        this.redisTemplate=redisTemplate;
        this.userDao=userDao;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=request.getHeader("Authorization");
        if(Strings.isEmpty(token)){
            filterChain.doFilter(request,response);
            return;
        }
        Optional<String> subFromToken = jwtService.getSubFromToken(token);
        if(!subFromToken.isPresent()){
            throw new BizException(HttpStatus.UNAUTHORIZED,"token解析失败，token有误或者不存在该用户");
        }
        String id="loginUser:"+subFromToken.get();
        String s = redisTemplate.opsForValue().get(id);
        if(Strings.isEmpty(s)){
            throw new BizException(HttpStatus.UNAUTHORIZED,"未登录！请先登录");
        }
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(User::getUsername,s);
        User user = userDao.selectOne(lqw);
        LoginUser loginUser=new LoginUser(user);
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);


    }
}
