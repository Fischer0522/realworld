package com.fischer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Random;

@RestController
@RequestMapping("email")
public class EmailApi {

    private StringRedisTemplate redisTemplate;

    private JavaMailSenderImpl javaMailSender;

    @Value("${mail.username}")
    private String username;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.password}")
    private String password;

    public EmailApi(StringRedisTemplate redisTemplate,JavaMailSenderImpl javaMailSender){
        this.javaMailSender=javaMailSender;
        this.redisTemplate=redisTemplate;
    }

    @GetMapping
    public ResponseEntity sendEmail(@RequestParam(value = "email") String userEmail){
        //验证码存入redis并设置过期时间
        double random = Math.random();
        int i=(int) (random*9999);
        redisTemplate.opsForValue().set(userEmail,i+"");
        Duration duration=Duration.ofMinutes(5);
        redisTemplate.expire(userEmail,duration);

        //将验证码发送给用户
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setHost(host);
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(userEmail);
        simpleMailMessage.setSubject("登录验证码");
        simpleMailMessage.setText("请在5分钟内尽快填写验证码，完成注册\n"+"验证码为："+i);
        javaMailSender.send(simpleMailMessage);
        return ResponseEntity.ok("验证码已经发送");
    }


}
