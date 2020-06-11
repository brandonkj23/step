package com.google.sps.servlets;

import com.google.sps.data.UserInfo;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet("/loggedin")
public class Loggedin extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    boolean isUserLoggedIn = userService.isUserLoggedIn();
    String urlToRedirectToAfterLoginChange = "/index.html";
    String email = "";
    String url = "";
      if(isUserLoggedIn){
       email = userService.getCurrentUser().getEmail();
       url = userService.createLogoutURL(urlToRedirectToAfterLoginChange);
      }else{
       url = userService.createLoginURL(urlToRedirectToAfterLoginChange);
      }
    UserInfo userInfo = new UserInfo(isUserLoggedIn,email,url);
      

    Gson gson = new Gson();
        
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(userInfo));
  }


}