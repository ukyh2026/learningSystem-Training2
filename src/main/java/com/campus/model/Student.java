package com.campus.model;


public class Student extends User{


    public Student(int userID, String username, String password) {

        super(userID, username, password, Role.STUDENT);
    }
}
