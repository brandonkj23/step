package com.google.sps.data;


public class UserInfo{

    private boolean isUserLoggedIn;
    private String email;

    public UserInfo(boolean isUserLoggedIn, String email){
        this.isUserLoggedIn = isUserLoggedIn;
        this.email = email;
    }
}