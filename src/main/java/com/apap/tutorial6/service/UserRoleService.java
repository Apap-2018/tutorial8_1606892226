package com.apap.tutorial6.service;

import com.apap.tutorial6.model.UserRoleModel;

public interface UserRoleService {
    UserRoleModel addUser(UserRoleModel user);
    void editPassword(String password, String username);
    UserRoleModel findByUsername(String username);
    public String encrypt(String password);
}