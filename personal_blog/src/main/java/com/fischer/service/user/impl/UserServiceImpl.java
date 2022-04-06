package com.fischer.service.user.impl;

import com.fischer.api.exception.BizException;
import com.fischer.assistant.EncryptService;
import com.fischer.data.RegisterParam;
import com.fischer.pojo.User;
import com.fischer.repository.ImageRepository;
import com.fischer.repository.UserRepository;
import com.fischer.data.UpdateUserCommand;
import com.fischer.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
@Validated
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private String defaultImage;
    private EncryptService encryptService;
    private ImageRepository imageRepository;

    @Autowired
    public UserServiceImpl (
            UserRepository userRepository,
            @Value("${image.default}") String defaultImage,
            EncryptService encryptService,
            ImageRepository imageRepository){
        this.defaultImage=defaultImage;
        this.userRepository=userRepository;
        this.encryptService=encryptService;
        this.imageRepository=imageRepository;
    };
    @Override
    public User createUser( @Valid RegisterParam registerParam) {
        User user=new User(
                registerParam.getEmail(),
                registerParam.getUsername(),
                registerParam.getPassword(),
                "这个人很懒，什么都没有写",
                defaultImage);
        userRepository.save(user);
        return user;
    }

    @Override
    public void updateUser(@Valid UpdateUserCommand command) {
        User user=command.getTargetUser();
        boolean b = imageRepository.removeByUserId(user.getImage());
        if(!b){
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,"原文件删除失败，请联系管理员");
        }
        user.update(
                command.getParam().getEmail(),
                command.getParam().getUsername(),
                command.getParam().getPassword(),
                command.getParam().getBio(),
                command.getParam().getImage()
        );

        userRepository.save(user);


    }
}
