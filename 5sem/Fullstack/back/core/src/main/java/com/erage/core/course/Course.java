package com.erage.core.course;

public class Course {
    private Long id;
    private String username;
    private String description;

    public Course(){
        id = (long) 0;
        username = "0";
        description = "0";
    }

    public Course(long l, String in28minutes, String s) {
        id = l;
        username = in28minutes;
        description = s;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}

