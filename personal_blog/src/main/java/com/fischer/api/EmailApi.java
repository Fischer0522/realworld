package com.fischer.api;

import com.fischer.api.exception.BizException;
import com.fischer.assistant.ResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import java.time.Duration;
import java.util.HashMap;
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
    public ResponseEntity sendEmail( @Validated @Email(message = "请填写格式正确的邮箱") @RequestParam(value = "email") String userEmail){
        if (!userEmail.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$" )){
            throw new BizException(HttpStatus.BAD_REQUEST,"请填写格式正确的邮箱");
        }
        //验证码存入redis并设置过期时间

        double random = Math.random();
        int i=(int) (999+random*9000);
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

        /*return ResponseEntity.ok(new HashMap<String,Object>(){
            {
                put("code",200);
                put("message","验证码已经成功发送");
            }
        });*/
        return ResponseEntity.ok(new ResultType(200,null,"验证码已经成功发送"));
    }


}
