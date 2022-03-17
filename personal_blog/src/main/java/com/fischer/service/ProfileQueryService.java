package com.fischer.service;

import com.fischer.data.ProfileData;
import com.fischer.data.UserData;
import com.fischer.pojo.User;
import com.fischer.readService.UserReadService;
import com.fischer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProfileQueryService {
    @Autowired
    private UserRepository userRepository;

    public Optional<ProfileData> finByUsername(String username){
        Optional<User> optional = userRepository.findByUsername(username);
        if(optional.isPresent()){
            User user= optional.get();
            ProfileData profileData=new ProfileData(user);
            return Optional.of(profileData);
        }
        else{
            return Optional.empty();
        }
    }

}
