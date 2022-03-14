package com.fischer.jwt;

import com.fischer.api.exception.BizException;
import com.fischer.dao.UserDao;
import com.fischer.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class DefaultJwtService implements JwtService{

    private int sessionTime;
    private String secret;
    private UserDao userDao;
    @Autowired
    public DefaultJwtService(
             @Value("${jwt.secret}") String secret,
             @Value("${jwt.sessionTime}") int sessionTime,
             UserDao userDao){
        this.secret=secret;
        this.sessionTime=sessionTime;
        this.userDao=userDao;
    }
    @Override
    public String toToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getId())
                .setExpiration(expireTimeFromNow())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return token;


    }

    @Override
    public Optional<String> getSubFromToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            return Optional.ofNullable(claimsJws.getBody().getSubject());
        } catch (Exception e){

            return Optional.empty();
        }
    }

    @Override
    public Optional<User> toUser(String token) {
        Optional<String> subFromToken = getSubFromToken(token);
        if(!subFromToken.isPresent()){
            throw new BizException(HttpStatus.UNAUTHORIZED,"token解析失败");
        }
        String id = subFromToken.get();
        User user = userDao.selectById(id);
        return Optional.ofNullable(user);

    }

    private Date expireTimeFromNow(){
        return new Date(System.currentTimeMillis()+sessionTime*1000);
    }


}
