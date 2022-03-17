package com.fischer.readService.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.UserDao;
import com.fischer.data.UserData;
import com.fischer.pojo.User;
import com.fischer.readService.UserReadService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserReadServiceImpl implements UserReadService {
    @Autowired
    private UserDao userDao;
    @Override
    public UserData findByUsername(String username) {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(username),User::getUsername,username);
        User user = userDao.selectOne(lqw);
        return new UserData(user);

    }

    @Override
    public UserData findById(String id) {
        User user = userDao.selectById(id);
        return new UserData(user);
    }
}
