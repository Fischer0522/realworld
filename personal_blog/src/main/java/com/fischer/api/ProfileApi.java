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
    @Autowired
    public ProfileApi(ProfileQueryService profileQueryService){
        this.profileQueryService=profileQueryService;
    }
    @GetMapping
    public ResponseEntity getProfile(@PathVariable("username") String username){
        return profileQueryService.finByUsername(username)
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
