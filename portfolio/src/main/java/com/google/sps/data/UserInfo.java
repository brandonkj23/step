package com.google.sps.data;


public class UserInfo{

    private boolean isUserLoggedIn;
    private String email;
    private String logInUrl;

    public UserInfo(boolean isUserLoggedIn, String email, String logInUrl){
        this.isUserLoggedIn = isUserLoggedIn;
        this.email = email;
        this.logInUrl = logInUrl;
    }
}