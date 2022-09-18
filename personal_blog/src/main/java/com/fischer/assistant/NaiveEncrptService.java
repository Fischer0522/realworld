package com.fischer.assistant;

import org.springframework.stereotype.Service;

@Service
public class NaiveEncrptService implements EncryptService {
    @Override
    public String encrypt(String password) {
       return password;
    }

    @Override
    public boolean check(String checkPassword, String realPassword) {
        return checkPassword.equals(realPassword);
    }
}
