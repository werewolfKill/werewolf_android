package com.zinglabs.zwerewolf.entity;

/**
 * author: vector.huang
 * date：2016/4/19 20:51
 */
public class User {

    private int id;
    private String username;

    public User(int id,String username){
        this.id = id;
        this.username = username;
    }
    public User(int id){
        this.id = id;
    }

    public User(String username){
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
