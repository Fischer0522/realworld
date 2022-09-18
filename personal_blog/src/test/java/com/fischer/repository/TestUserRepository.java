package com.fischer.repository;

import com.fischer.pojo.User;
import com.fischer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class TestUserRepository {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void testFindById(){
        Optional<User> user = userRepository.findById("1234");
        System.out.println(user.get());
    }
    @Test
    public void testFindByName(){
        String name="fischer";
        Optional<User> user = userRepository.findByUsername(name);
        System.out.println(user.get());
    }
    @Test
    public void testFindByEmail(){
        String email="05203744@cumt.edu.cn";
        Optional<User> user = userRepository.findByEmail(email);
        System.out.println(user.get());
    }

}
