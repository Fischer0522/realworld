package com.fischer.data;


import com.fischer.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserData {
    private String id;
    private String email;
    private String username;
    private String bio;
    private String image;
    /* private String id;
    private String email;
    private String username;
    private String password;
    private String bio;
    private String image;
*/
    public UserData(User user)
    {
        this.id=user.getId();
        this.email=user.getEmail();
        this.username= user.getUsername();
        this.bio=user.getBio();
        this.image=user.getImage();
    }
}
