package com.android.freelance.retrofitexample.data.models;

public class Posts {

    private int userId;
    /*private int id;*/
    private Integer id;
    private String title;
    private String body;

    /*public Posts(int userId, int id, String title, String body) {*/
    public Posts(int userId, String title, String body) {
        this.userId = userId;
        /*this.id = id;*/
        this.title = title;
        this.body = body;
    }

   /* public int getUserId() {*/
   public Integer getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
