package com.bigo.tindatrack.data.models;

import java.io.Serializable;

public class User implements Serializable {
    private String username, fullname, password, email, phoneNumber, storeName;

    public User(String username, String fullname, String password, String email, String phoneNumber, String storeName) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.storeName = storeName;

    }

    public String getUsername() {return username;}
    public String getFullname() {
        return fullname;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getStoreName() {
        return storeName;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
