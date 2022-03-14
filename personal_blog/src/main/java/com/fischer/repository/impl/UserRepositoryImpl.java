package com.fischer.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.UserDao;
import com.fischer.pojo.User;
import com.fischer.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private UserDao userDao;
    @Override
    public void save(User user) {
        if(userDao.selectById(user.getId())==null)
        {
            userDao.insert(user);
        }
        else
        {
            userDao.updateById(user);
        }

    }
    @Override
    public Optional<User> findById(String id) {
        User user = userDao.selectById(id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(username),User::getUsername,username);
        User user = userDao.selectOne(lqw);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(email),User::getEmail,email);
        User user = userDao.selectOne(lqw);
        return Optional.ofNullable(user);
    }
}
