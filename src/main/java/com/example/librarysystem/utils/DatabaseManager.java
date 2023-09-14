package com.example.librarysystem.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {

    private static final String DBNAME = "library";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final String HOSTNAME = "localhost";
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            String url = "jdbc:mysql://" + HOSTNAME + "/" + DBNAME;
            Connection connection = DriverManager.getConnection(url, USER, PASS);
            System.out.println("Connected Database...");
            return connection;
        } catch (Exception e) {
            System.out.println("Not Connected Database...");
        }
        return null;
    }
}