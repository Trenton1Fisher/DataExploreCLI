package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {
    private String url;
    private Connection connection;

    public SQLiteManager(String dbUrl) {
        this.url = dbUrl;
    }

    public void printUrl(){
        System.out.println(this.url);
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void executeUpdate(String sql) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet executeQuery(String sql) {
        ResultSet resultSet = null;
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return resultSet;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection to SQLite has been closed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
