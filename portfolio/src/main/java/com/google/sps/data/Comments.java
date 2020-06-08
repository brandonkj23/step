package com.google.sps.data;


public class Comments{

    private final long id;
    private final String comment;
    private final long timestamp;
    private final String name;

    public Comments(long id, String comment, long timestamp, String name){
        this.id = id;
        this.comment = comment;
        this.timestamp = timestamp;
        this.name = name;
    }

}