package com.erage.core.landing;

import com.erage.core.dataBase.manageDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;


@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })

@RestController
public class UserResource {

    @Autowired
    private manageDataBase manageDB;
//    private UserHardcoded userManagement;

//    @PutMapping("/LandingEnterPage")
//    public String checkPassword(@RequestBody User user) {
//        return userManagement.tryLogin(user);
//    }
//    @PutMapping("/LandingRegistration")
//    public String registerUser(@RequestBody User user) {
//        return userManagement.registerUser(user);
//    }

    @PutMapping("/LandingEnterPage")
    public String checkPassword(@RequestBody User user) throws ParseException, SQLException, ClassNotFoundException {
        return manageDB.tryLogin(user);
    }
    @PutMapping("/LandingRegistration")
    public String registerUser(@RequestBody User user) throws IllegalAccessException, SQLException, ClassNotFoundException {
        return manageDB.registerUser(user);
    }

}
