package com.fischer.api;

import com.fischer.api.exception.BizException;

import com.fischer.data.LoginParam;
import com.fischer.data.RegisterParam;
import com.fischer.data.UserData;
import com.fischer.data.UserWithToken;
import com.fischer.jwt.JwtService;
import com.fischer.pojo.User;
import com.fischer.repository.UserRepository;
import com.fischer.service.user.UserQueryService;
import com.fischer.service.user.UserService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Validated
@RestController
public class UsersApi {
    private UserRepository userRepository;
    private UserQueryService userQueryService;
    private JwtService jwtService;
    private UserService userService;
    private StringRedisTemplate redisTemplate;

    @Autowired
    public UsersApi(
            UserRepository userRepository,
            UserQueryService userQueryService,
            JwtService jwtService,
            UserService userService,
            StringRedisTemplate redisTemplate,
            @Value("${jwt.sessionTime}")int sessionTime

    ){
        this.userRepository=userRepository;
        this.userQueryService=userQueryService;
        this.jwtService=jwtService;
        this.userService=userService;
        this.redisTemplate=redisTemplate;

    }

    @PostMapping(path = "/users/register")
    public ResponseEntity createUser(@Valid @RequestBody RegisterParam registerParam){

        User user=userService.createUser(registerParam);
        UserData userData = userQueryService.finById(user.getId()).get();

        Map<String,Object> map=new HashMap<>();
        map.put("user",userData);
        return ResponseEntity.status(201).body(map);

    }

    @PostMapping(path = "/users/login")
    public ResponseEntity userLogin(@Valid @RequestBody LoginParam loginParam){

        Optional<User> byEmail = userRepository.findByEmail(loginParam.getEmail());
        if(byEmail.isPresent()&&
                loginParam.getPassword()
                        .equals(byEmail.get().getPassword())){
            User user = byEmail.get();
            String loginId="loginUser:"+user.getId();
            redisTemplate.opsForValue().set(loginId,user.getUsername());
            Duration duration=Duration.ofDays(7);
            redisTemplate.expire(loginId,duration);
            UserData userData = userQueryService.finById(user.getId()).get();

            return ResponseEntity
                    .ok(userResponse(new UserWithToken(userData,jwtService.toToken(user))));
        }
        else{
            throw new BizException(HttpStatus.UNAUTHORIZED,"邮箱或者密码错误！");
        }

    }
    @DeleteMapping("/users/logout")
    public ResponseEntity userLogout(@RequestHeader(value = "token")String token){
        String id = jwtService.getSubFromToken(token).get();
        String logoutId="loginUser:"+id;
        Boolean delete = redisTemplate.delete(logoutId);
        if(delete==false){
            throw new BizException(HttpStatus.UNAUTHORIZED,"用户已经注销，请勿重新操作");
        }
        return ResponseEntity.noContent().build();
    }



    private Map<String,Object> userResponse(UserWithToken userWithToken){
        return new HashMap<String,Object>(){
            {
                put("user",userWithToken);
            }
        };
    }




}
