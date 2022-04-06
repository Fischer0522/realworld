package com.fischer.api;

import com.fischer.api.exception.BizException;

import com.fischer.assistant.ResultType;
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
@RequestMapping("users")
public class UsersApi {
    private UserQueryService userQueryService;
    private UserService userService;
    private StringRedisTemplate redisTemplate;

    @Autowired
    public UsersApi(
            UserRepository userRepository,
            UserQueryService userQueryService,
            JwtService jwtService,
            UserService userService,
            StringRedisTemplate redisTemplate

    ){
        this.userQueryService=userQueryService;
        this.userService=userService;
        this.redisTemplate=redisTemplate;

    }

    @PostMapping(path = "register")
    public ResponseEntity createUser(@Valid @RequestBody RegisterParam registerParam){

        String verifyCode = registerParam.getVerifyCode();
        String s = redisTemplate.opsForValue().get(registerParam.getEmail());

        if(!verifyCode.equals(s)){

            throw new BizException(HttpStatus.BAD_REQUEST,"验证码错误,请重新获取");
        }

        User user=userService.createUser(registerParam);
        UserData userData = userQueryService.finById(user.getId()).get();

        Map<String,Object> map=new HashMap<>();
        map.put("user",userData);
        /*return ResponseEntity.status(201).body(map);*/
        return ResponseEntity.status(200).body(new ResultType(201,map,"ok"));

    }

    @PostMapping(path = "login")
    public ResponseEntity userLogin(@Valid @RequestBody LoginParam loginParam){

       /* Optional<User> byEmail = userRepository.findByEmail(loginParam.getEmail());
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
        }*/
        UserWithToken user = userService.loginUser(loginParam);
        return ResponseEntity
                .ok(new ResultType(200,user,"登录成功"));

    }
    @DeleteMapping("logout")
    public ResponseEntity userLogout(@RequestHeader(value = "Authorization")String token){
        /*String id = jwtService.getSubFromToken(token).get();
        String logoutId="loginUser:"+id;
        Boolean delete = redisTemplate.delete(logoutId);
        if(Boolean.FALSE.equals(delete)){
            throw new BizException(HttpStatus.UNAUTHORIZED,"用户已经注销，请勿重新操作");
        }
        return ResponseEntity.ok(new ResultType(204,null,"ok"));*/
        userService.logoutUser(token);
        return ResponseEntity.ok(new ResultType(204,null,"注销成功"));
    }



    private ResultType userResponse(UserWithToken userWithToken){
        /*return new HashMap<String,Object>(){
            {
                put("user",userWithToken);
            }
        };*/
        return new ResultType(200,userWithToken,"ok");
    }




}
