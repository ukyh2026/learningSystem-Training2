package com.campus.model;


import java.util.HashSet;
import java.util.Set;

public abstract class  User {
    int userID;
    String username;
    String password;
    Role role;
    Set<String> tags;
    String faceImage;

    public User(int userID, String username, String password, Role role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
        this.tags = new HashSet<>();
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public Set<String> getTags() {
        return tags;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void addTag(String tag){
        this.tags.add(tag);
    }
    public void removeTag(String tag){
        this.tags.remove(tag);
    }


}
