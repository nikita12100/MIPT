package com.erage.core.landing;

import javax.swing.text.StyledEditorKit;

public class User {
    String client;
    String name;
    String surname;
    String email;
    String password;
    Boolean emailNotifications;

    public User(){
        client = "0";
        name = "0";
        surname = "0";
        email = "0";
        password = "0";
        emailNotifications = false;
    }

    public User(String name, String password){
        this.name = name;
        this.password = password;
        client = "0";
        surname = "0";
        email = "0";
        emailNotifications = false;
    }

    public User(String client,
                String name,
                String surname,
                String email,
                String password,
                Boolean emailNotifications){
        this.client = client;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.emailNotifications = emailNotifications;
    }

    public User(User user){
        this(
                user.getClient(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword(),
                user.getEmailNotifications());
    }

    public String getClient() {
        return client;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

}
