
package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;

public class comments{
    private final List<String> history = new ArrayList<>();

    private void logComment(String  msg){
        history.add(msg)
    }
}