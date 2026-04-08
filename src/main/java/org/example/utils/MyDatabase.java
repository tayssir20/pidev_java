package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
<<<<<<< HEAD
    private final String url="jdbc:mysql://localhost:4307/esport-db";
    private final String user="root";
    private final String password="";
    private Connection connection;
=======
>>>>>>> a3677fbf5857f4bda661850b38e7b4f290dd8d53
    private static MyDatabase instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/esport-db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    private MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie ");
        } catch (SQLException e) {
            System.out.println(" Erreur connexion : " + e.getMessage());
        }
    }

    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}