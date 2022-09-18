package com.fischer.service.user;

import com.fischer.data.UserData;
import com.fischer.readService.UserReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserQueryService {
    @Autowired
    private UserReadService userReadService;

    public Optional<UserData> finById(String id){
        UserData userData = userReadService.findById(id);
        return Optional.ofNullable(userData);
    }


}
