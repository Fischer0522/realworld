package com.fischer.readService;

import com.fischer.data.UserData;

public interface UserReadService {

    UserData findByUsername(String username);
    UserData findById(String id);
}
