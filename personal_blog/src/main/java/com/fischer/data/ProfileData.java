package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fischer.pojo.User;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ProfileData {
    @JsonIgnore
    private String id;
    private String username;
    private String bio;
    private String image;
    //private boolean following;
    public ProfileData(User user)
    {
        this.id=user.getId();
        this.username=user.getUsername();
        this.bio=user.getBio();
        this.image=user.getImage();
    }

}
