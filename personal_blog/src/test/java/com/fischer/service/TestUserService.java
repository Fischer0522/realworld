package com.fischer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.UserDao;
import com.fischer.data.RegisterParam;
import com.fischer.data.UpdateUserParam;
import com.fischer.pojo.User;
import com.fischer.data.UpdateUserCommand;
import com.fischer.service.user.UserService;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestUserService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Test
    public void testCreateUser(){


        RegisterParam registerParam=new RegisterParam(
                "18763169159@qq.com",
                "Hades",
                "123"
        );
        User user = userService.createUser(registerParam);
        System.out.println(user);

    }
    @Test
    public void testUpdateUser(){

        UpdateUserParam updateUserParam=new UpdateUserParam(
                "18763169159@qq.com",
                "123",
                "John Wick",
                "babayaga",
                "https://static.productionready.io/images/smiley-cyrus.jpg");
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty("John Wick"),User::getUsername,"John Wick");
        User user = userDao.selectOne(lqw);
        UpdateUserCommand updateUserCommand=new UpdateUserCommand(user,updateUserParam);
        userService.updateUser(updateUserCommand);
    }

    @Test
    public void testSelectUserList(){
        for (User user : userDao.selectList(null)) {
            System.out.println(user);
        }

    }
}
