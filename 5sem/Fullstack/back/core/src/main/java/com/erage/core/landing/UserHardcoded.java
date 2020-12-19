package com.erage.core.landing;

import com.erage.core.course.Course;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserHardcoded {

    private static User user1 = new User("nikita", "123");

    public String getUserPwd(String user){
        if(user1.getName().equals(user)){
            return user1.getPassword();
        }
        else {
            return "None";
        }
    }

    public String tryLogin(User user){
        if(user1.name.equals(user.getName()) && user1.password.equals(user.getPassword())){
            return "Success";
        }
        else {
            return "Fail";
        }
    }

}
