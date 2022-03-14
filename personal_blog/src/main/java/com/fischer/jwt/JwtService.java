package com.fischer.jwt;

import com.fischer.pojo.User;

import java.util.Optional;

public interface JwtService {

    String toToken(User user);

    Optional<String> getSubFromToken(String token);

    Optional<User> toUser(String token);


}
