package com.erage.core.dataBase;

import com.erage.core.landing.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@Service
public class manageDataBase {

    private static DataBase db = new DataBase();

    manageDataBase() throws SQLException, IOException, ClassNotFoundException {
        db.createAllTables();

    }

    public String tryLogin(User user) throws ParseException, SQLException, ClassNotFoundException {
        String answer = "Fail";
        try {
            for (User u : db.getAllUsers()) {
                if (u.getName().equals(user.getName()) &&
                        u.getPassword().equals(user.getPassword())) {
                    answer = "Success";
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Fail login user to dataBase.");
        }
        return answer;
    }

    public String registerUser(User user) throws IllegalAccessException, SQLException, ClassNotFoundException {
        try {
            db.setUser(user);
        } catch (Exception e) {
            System.out.println("Fail adding user to dataBase.");
        }

        return "Success";
    }

}
