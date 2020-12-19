package com.erage.core.landing;

import com.erage.core.course.Course;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserHardcoded {

    private static List<User> users = new ArrayList<>();

    UserHardcoded() {
        users.add(new User("nikita", "123"));
    }

    public String getUserPwd(String user) {
        if (users.get(0).getName().equals(user)) {
            return users.get(0).getPassword();
        } else {
            return "None";
        }
    }

    public String tryLogin(User user) {
        for (User u : users) {
            if (u.getName().equals(user.getName()) &&
                    u.getPassword().equals(user.getPassword())) {
                return "Success";
            }
        }
        return "Fail";
    }

    public String registerUser(User user) {
        users.add(new User(user));

        return "Success";
    }

}
