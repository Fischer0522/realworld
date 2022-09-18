package com.fischer.data;

import com.fischer.data.UpdateUserParam;
import com.fischer.pojo.User;
import com.fischer.service.user.constraintValidator.UpdateUserConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@UpdateUserConstraint
public class UpdateUserCommand {
    private User targetUser;
    private UpdateUserParam param;
}
