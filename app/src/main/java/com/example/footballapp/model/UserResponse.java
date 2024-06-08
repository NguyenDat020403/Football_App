package com.example.footballapp.model;

public class UserResponse {
    private String userId;
    private String name;
    private String email;
    public String photoUrl;

    public UserResponse(String userId, String name, String email, String photoUrl) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
