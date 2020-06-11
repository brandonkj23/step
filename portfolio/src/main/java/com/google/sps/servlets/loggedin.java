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
public class loggedin extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    Query query = new Query("loginInfo");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
   
    //Entity entity = results.asSingleEntity();
    UserInfo userInfo = new UserInfo();
    for (Entity entity : results.asIterable()) {
      boolean isUserLoggedIn = (boolean) entity.getProperty("isUserLoggedIn");
      String email = (String) entity.getProperty("email");
      userInfo = new UserInfo(isUserLoggedIn,email);
    }
      

    Gson gson = new Gson();
        
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(userInfo));
  }

  @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{

        UserService userService = UserServiceFactory.getUserService();

        Entity userEntity = new Entity("loginInfo");
        userEntity.setProperty("isUserLoggedIn",userService.isUserLoggedIn());
        userEntity.setProperty("email",userService.getCurrentUser().getEmail());

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(userEntity);

        response.sendRedirect("/index.html");
    }
}