package com.fischer.service.user;

import com.fischer.data.RegisterParam;
import com.fischer.data.UpdateUserCommand;
import com.fischer.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
@Validated
@Service
public interface UserService {

    User createUser(@Valid RegisterParam registerParam);

    void updateUser(@Valid UpdateUserCommand command);





}
