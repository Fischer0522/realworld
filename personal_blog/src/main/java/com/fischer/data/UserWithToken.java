package com.fischer.data;

import com.fischer.pojo.User;
import lombok.Getter;

@Getter
public class UserWithToken {
    private String email;
    private String username;
    private String bio;
    private String image;
    private String token;

    public UserWithToken(UserData userData, String token) {
        this.email = userData.getEmail();
        this.username = userData.getUsername();
        this.bio = userData.getBio();
        this.image = userData.getImage();
        this.token = token;
    }
    public UserWithToken(User user, String token){
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.image = user.getImage();
        this.token = token;

    }

}
