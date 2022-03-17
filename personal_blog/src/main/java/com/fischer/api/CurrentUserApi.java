package com.fischer.api;

import com.fischer.api.exception.BizException;
import com.fischer.data.UpdateUserCommand;
import com.fischer.data.UpdateUserParam;
import com.fischer.data.UserData;
import com.fischer.data.UserWithToken;
import com.fischer.jwt.JwtService;
import com.fischer.pojo.User;
import com.fischer.service.user.UserQueryService;
import com.fischer.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class CurrentUserApi {

    private UserQueryService userQueryService;
    private UserService userService;
    private JwtService jwtService;

    @Autowired
    public CurrentUserApi(
            UserQueryService userQueryService,
            UserService userService,
            JwtService jwtService
    ){
        this.userQueryService=userQueryService;
        this.userService=userService;
        this.jwtService=jwtService;
    }

    @GetMapping
    public ResponseEntity currentUser(@RequestHeader(value = "Authorization")String token){
        Optional<String> subFromToken = jwtService.getSubFromToken(token);
        if(subFromToken.isPresent()){
            String id=subFromToken.get();
            UserData userData = userQueryService.finById(id).get();
            return ResponseEntity
                    .ok(userResponse(new UserWithToken(userData,token)));
        }
        else{
            throw new BizException(HttpStatus.UNAUTHORIZED,"token解析失败");
        }

    }
    @PutMapping
    public ResponseEntity updateProfile(
            @RequestHeader(value = "Authorization") String token,
            @Valid @RequestBody UpdateUserParam updateUserParam){
        User currentUser = jwtService.toUser(token).get();
        String userId = currentUser.getId();
        UpdateUserCommand updateUserCommand=new UpdateUserCommand(currentUser,updateUserParam);
        userService.updateUser(updateUserCommand);
        UserData userData = userQueryService.finById(userId).get();
        return ResponseEntity.ok(userResponse(new UserWithToken(userData,token)));

    }
    private Map<String,Object> userResponse(UserWithToken userWithToken){
        return new HashMap<String,Object>(){
            {
                put("user",userWithToken);
            }
        };
    }
}
