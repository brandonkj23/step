// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.sps.data.Comments;
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Comments").addSort("timestamp", SortDirection.DESCENDING);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        int max = Integer.parseInt(getParameter(request,"max-number","5"));

        List<Comments> comments = new ArrayList<>();
        int counter = 0;
        for (Entity entity : results.asIterable()) {  
            long id = entity.getKey().getId();         
            String comment = (String) entity.getProperty("comment");
            long timestamp = (long) entity.getProperty("timestamp");
            String name = (String) entity.getProperty("name");
            if(max > counter){
                Comments cmt = new Comments(id,comment,timestamp,name);
                comments.add(cmt);
            }
            counter++;
        }

        Gson gson = new Gson();
        
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(comments));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        UserService userService = UserServiceFactory.getUserService();
        String text = request.getParameter("text-input");
        String name = request.getParameter("name-input");

        Entity commentEntity = new Entity("Comments");
        commentEntity.setProperty("timestamp", System.currentTimeMillis());
        commentEntity.setProperty("comment",text);
        commentEntity.setProperty("name","");
        if(userService.isUserLoggedIn()){
            commentEntity.setProperty("name",userService.getCurrentUser().getEmail());
        }

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);

        response.sendRedirect("/index.html");
    }


    private String convertToJsonUsingGson(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
        return defaultValue;
        }
        return value;
    }


}
