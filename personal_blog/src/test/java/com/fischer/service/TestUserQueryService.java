package com.fischer.service;

import com.fischer.data.UserData;
import com.fischer.service.user.UserQueryService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class TestUserQueryService {
    @Autowired
    private UserQueryService userQueryService;
    @Test
    public void testFindById(){
        Optional<UserData> userData = userQueryService.finById("1234");
        System.out.println(userData.get());
    }
}
