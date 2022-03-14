package com.fischer.jwt;

import com.fischer.dao.UserDao;
import com.fischer.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class token {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDao userDao;
    @Test
    void test1(){
        String token="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlZjZlMDAxOS00NDM5LTRjNDkt" +
                "YmRiYS1hNzRkNWU5MTUzMTAiL" +
                "CJleHAiOjE2NDcyNTAxMzR9.EV2xQ8m" +
                "OLNXl6uctq6-sF2VCYwBCp21BSgAW1IIgsW0";
        Optional<User> user = jwtService.toUser(token);
        System.out.println(user.get());

    }
}
