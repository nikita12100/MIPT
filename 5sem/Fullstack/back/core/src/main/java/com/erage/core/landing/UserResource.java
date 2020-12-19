package com.erage.core.landing;

import com.erage.core.course.Course;
import com.erage.core.course.CoursesHardcodedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })

@RestController
public class UserResource {

    @Autowired
    private UserHardcoded userManagement;

//    @GetMapping("/LandingEnterPage")
//    public String getUserPwd() {
//        return userManagement.getUserPwd("23");
//    }

    @PutMapping("/LandingEnterPage")
    public String checkPassword(@RequestBody User user) {
        return userManagement.tryLogin(user);
    }


}
