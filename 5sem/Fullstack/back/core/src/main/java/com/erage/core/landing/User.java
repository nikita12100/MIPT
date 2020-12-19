package com.erage.core.landing;

public class User {
    String name;
    String password;

    public User(){
        name = "0";
        password = "0";
    }

    public User(String name, String password){
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
