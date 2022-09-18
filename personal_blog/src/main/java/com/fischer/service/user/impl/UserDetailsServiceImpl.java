package com.fischer.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.UserDao;
import com.fischer.pojo.User;
import com.fischer.security.LoginUser;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(username),User::getEmail,username);
        User user = userDao.selectOne(lqw);
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("邮箱或者密码错误");
        }
        return new LoginUser(user);

    }
}
