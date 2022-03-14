package com.fischer.api;

import com.fischer.api.exception.BizException;
import com.fischer.data.ProfileData;
import com.fischer.jwt.JwtService;
import com.fischer.pojo.User;
import com.fischer.service.ProfileQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(path = "profiles/{username}")
public class ProfileApi {
    private ProfileQueryService profileQueryService;
    private JwtService jwtService;

    @Autowired
    public ProfileApi(
            ProfileQueryService profileQueryService,
            JwtService jwtService){
        this.profileQueryService=profileQueryService;
        this.jwtService=jwtService;
    }

    @GetMapping
    public ResponseEntity getProfile(@PathVariable("username") String username,
                                     @RequestHeader(value = "token")String token){
        Optional<User> user = jwtService.toUser(token);
        return profileQueryService.finByUsername(username,user.get())
                .map(this::profileResponse)
                .orElseThrow(()->new BizException(HttpStatus.NOT_FOUND,"该用户已不存在"));
    }
    /*后续补充关注和取关的功能*/
    private ResponseEntity profileResponse(ProfileData profile) {
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("profile", profile);
        }});
    }


}
