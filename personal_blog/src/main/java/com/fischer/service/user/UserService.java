package com.fischer.service.user;

import com.fischer.data.LoginParam;
import com.fischer.data.RegisterParam;
import com.fischer.data.UpdateUserCommand;
import com.fischer.data.UserWithToken;
import com.fischer.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
@Validated
@Service
public interface UserService {

    User createUser(@Valid RegisterParam registerParam);

    void updateUser(@Valid UpdateUserCommand command);

    UserWithToken loginUser(@Valid LoginParam loginParam);

    void logoutUser(String token);





}
