package com.campus.model;

public class Teacher  extends User{
    public Teacher(int userID, String username, String password) {

        super(userID, username, password, Role.TEACHER);
    }
}
