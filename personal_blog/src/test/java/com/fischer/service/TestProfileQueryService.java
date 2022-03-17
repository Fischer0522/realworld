package com.fischer.service;

import com.fischer.dao.UserDao;
import com.fischer.data.ProfileData;
import com.fischer.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@SpringBootTest
public class TestProfileQueryService {
    @Autowired
    private ProfileQueryService profileQueryService;
    @Autowired
    private UserDao userDao;
    @Test
    public void testFindByUserName(){
        User user = userDao.selectById("1234");
        Optional<ProfileData> fischer = profileQueryService.finByUsername("fischer");
        System.out.println(fischer.get());
    }

}
