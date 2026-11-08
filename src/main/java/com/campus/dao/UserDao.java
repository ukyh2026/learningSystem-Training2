package com.campus.dao;

import com.campus.model.User;

import java.util.List;

public interface UserDao {
    int generateUserId();
    void addUser(User user);
    User getUserById(int userID);
    User getUserByUsername(String username);
    List<User> getAllUser();

}
