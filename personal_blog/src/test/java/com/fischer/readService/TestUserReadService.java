package com.fischer.readService;

import com.fischer.data.UserData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestUserReadService {
    @Autowired
    private UserReadService userReadService;
    @Test
    public void testFindByUserName(){
        UserData fischer = userReadService.findByUsername("fischer");
        System.out.println(fischer);
    }
    @Test
    public void testFindById(){
        UserData byId = userReadService.findById("1234");
        System.out.println(byId);
    }

}
