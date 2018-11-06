package com.apap.tutorial6.service;
import com.apap.tutorial6.repository.UserRoleDb;
import com.apap.tutorial6.model.UserRoleModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService{
    @Autowired
    private UserRoleDb userDb;

    @Override
    public UserRoleModel addUser(UserRoleModel user) {
        String pass = encrypt(user.getPassword());
        user.setPassword(pass);
        return userDb.save(user);
    }

    @Override
    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public UserRoleModel findByUsername(String username) {
        return userDb.findByUsername(username);
    }

    @Override
    public void editPassword(String password, String username) {
        UserRoleModel user = findByUsername(username);
        String hashedPassword = encrypt(password);
        user.setPassword(hashedPassword);
        userDb.save(user);
    }
}