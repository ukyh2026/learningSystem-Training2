package com.campus.dao.impl;

import com.campus.dao.UserDao;
import com.campus.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryUserDao implements UserDao {
    Map<Integer,User> userTable = new HashMap<>();
    private int nextId = 1;//nextId 是 InMemoryUserDao 的成员变量，
                            // 但整个系统共享一个 InMemoryUserDao 实例。Student 和 Teacher 不各自维护编号。
    @Override
    public int generateUserId() {
        return nextId++;
    }

    @Override
    public void addUser(User user) {
        userTable.put(user.getUserID(),user);
    }

    @Override
    public User getUserById(int userID) {
        return null;
    }


    @Override
    public User getUserByUsername(String username) {
        for(User user:userTable.values()){
            user.getUsername().equals(username);
            return user;
        }
        return null;}

    @Override
    public List<User> getAllUser() {
        return new ArrayList<>(userTable.values());
    }
}
