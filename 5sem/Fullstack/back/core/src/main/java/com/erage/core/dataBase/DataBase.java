package com.erage.core.dataBase;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;

import com.erage.core.landing.User;
import org.apache.commons.dbcp2.BasicDataSource;


public class DataBase {

    private  Connection connDB;
    private  Statement statmt;
    private  ResultSet resSet;
    private  BasicDataSource dataSource;
    private  String nameDB;
    private  String nameTableUser;

    DataBase(){
        nameDB = "ErageData";
        nameTableUser = "Users";
    }

    public  void createAllTables() throws SQLException, ClassNotFoundException, IOException {
        connDB(nameDB);
        createTable("'client' CHAR(20)," +
                "'name' CHAR(20)," +
                "'surname' CHAR(20)," +
                "'email' VARCHAR(30)," +
                "'password' TEXT," +
                "'emailNotifications' INT", nameTableUser);

        closeDB();
    }

    private  void connDB(String dbName) throws ClassNotFoundException, SQLException {
        try {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.sqlite.JDBC");
            dataSource.setUrl("jdbc:sqlite:db/" + dbName + ".s3db");
            dataSource.setMinIdle(5);
            dataSource.setMaxIdle(10);
            dataSource.setMaxOpenPreparedStatements(100);

            connDB = dataSource.getConnection();
            statmt = connDB.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("База " + dbName + " подключена!");
    }

    private  void createTable(String tableHead, String dbName) throws ClassNotFoundException, SQLException {
        statmt.execute("CREATE TABLE IF NOT EXISTS '" + dbName + "' (" + tableHead + ");");

        System.out.println("Таблица " + dbName + " создана или уже существует.");
    }

    public  void setUser(User user) throws SQLException, ClassNotFoundException, IllegalAccessException {
        connDB(nameDB);

        statmt.execute("INSERT INTO '" + nameTableUser + "' ('client', 'name', 'surname', 'email', 'password', 'emailNotifications') " +
                "VALUES " + user.getUserForDB());

        closeDB();
    }

    public  List<User> getAllUsers() throws SQLException, ClassNotFoundException, ParseException {
        connDB(nameDB);
        resSet = statmt.executeQuery("SELECT client, name, surname, email, password, emailNotifications FROM " + nameTableUser);

        List<User> users = new ArrayList<>();
        while (resSet.next()) {
            String client = resSet.getString("client");
            String name = resSet.getString("name");
            String surname = resSet.getString("surname");
            String email = resSet.getString("email");
            String password = resSet.getString("password");
            int emailNotifications = resSet.getInt("emailNotifications");
            users.add(new User(client, name, surname, email, password, emailNotifications != 0));
        }
        closeDB();

        return users;
    }

    public  User getUser(String name_) throws SQLException, ClassNotFoundException, ParseException {
        connDB(nameDB);
        resSet = statmt.executeQuery("SELECT client, name, surname, email, password, emailNotifications FROM " + nameTableUser);

        User user = null;
        while (resSet.next()) {
            String client = resSet.getString("client");
            String name = resSet.getString("name");
            String surname = resSet.getString("surname");
            String email = resSet.getString("email");
            String password = resSet.getString("password");
            int emailNotifications = resSet.getInt("emailNotifications");

            if (name.equals(name_)) {
                user = new User(client, name, surname, email, password, emailNotifications != 0);
                break;
            }
        }
        closeDB();

        return user;
    }


    private  void closeDB() throws ClassNotFoundException, SQLException {
        statmt.close();
        connDB.close();
        if (resSet != null) {
            resSet.close();
        }

        System.out.println("Соединения  закрыты");
        System.out.println();
    }

}
