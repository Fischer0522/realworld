package com.fischer.service.user.impl;

import com.fischer.api.exception.BizException;
import com.fischer.assistant.EncryptService;
import com.fischer.data.LoginParam;
import com.fischer.data.RegisterParam;
import com.fischer.data.UserWithToken;
import com.fischer.jwt.JwtService;
import com.fischer.pojo.User;
import com.fischer.repository.ImageRepository;
import com.fischer.repository.UserRepository;
import com.fischer.data.UpdateUserCommand;
import com.fischer.security.LoginUser;
import com.fischer.service.user.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Objects;

@Validated
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private String defaultImage;
    private EncryptService encryptService;
    private ImageRepository imageRepository;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private StringRedisTemplate redisTemplate;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl (
            UserRepository userRepository,
            @Value("${image.default}") String defaultImage,
            EncryptService encryptService,
            ImageRepository imageRepository,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            StringRedisTemplate redisTemplate,
            PasswordEncoder passwordEncoder){
        this.defaultImage=defaultImage;
        this.userRepository=userRepository;
        this.encryptService=encryptService;
        this.imageRepository=imageRepository;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
        this.redisTemplate=redisTemplate;
        this.passwordEncoder=passwordEncoder;
    };
    @Override
    public User createUser( @Valid RegisterParam registerParam) {
        String password = passwordEncoder.encode(registerParam.getPassword());
        User user=new User(
                registerParam.getEmail(),
                registerParam.getUsername(),
                password,
                "这个人很懒，什么都没有写",
                defaultImage);
        userRepository.save(user);
        return user;
    }

    @Override
    public void updateUser(@Valid UpdateUserCommand command) {
        User user=command.getTargetUser();
        boolean b =true;
        if (Strings.isNotEmpty(command.getParam().getImage())&&command.getTargetUser().getImage()!=defaultImage){
            b=imageRepository.removeByUserId(user.getImage());
        }

        String password = passwordEncoder.encode(command.getParam().getPassword());
        if(!b){
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,"原文件删除失败，请联系管理员");
        }
        user.update(
                command.getParam().getEmail(),
                command.getParam().getUsername(),
                password,
                command.getParam().getBio(),
                command.getParam().getImage()
        );

        userRepository.save(user);


    }

    @Override
    public UserWithToken loginUser(LoginParam loginParam) {
        UsernamePasswordAuthenticationToken authenticationToken
                =new UsernamePasswordAuthenticationToken(
                        loginParam.getEmail(),
                        loginParam.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new BizException(HttpStatus.UNAUTHORIZED,"邮箱或者密码错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        User user = loginUser.getUser();
        String token = jwtService.toToken(user);
        String key="loginUser:"+user.getId();
        redisTemplate.opsForValue().set(key,user.getUsername());
        Duration duration=Duration.ofDays(7);
        redisTemplate.expire(key,duration);
        return new UserWithToken(user,token);

    }

    @Override
    public void logoutUser(String token) {
        String id = jwtService.getSubFromToken(token)
                .orElseThrow(() -> new BizException(HttpStatus.INTERNAL_SERVER_ERROR, "token无法解析，注销失败"));
        String key="loginUser:"+id;
        Boolean delete = redisTemplate.delete(key);
        if(Boolean.FALSE.equals(delete)){
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,"redis异常，注销失败");
        }


    }
}
