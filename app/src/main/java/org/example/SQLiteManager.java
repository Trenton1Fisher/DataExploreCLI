package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


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
            connection = DriverManager.getConnection(this.url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadData(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             CSVParser csvParser = new CSVParser(inputStreamReader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            if (inputStream == null) {
                throw new FileNotFoundException("File not found: " + fileName);
            }

           // csvParser.forEach(record -> System.out.println(record));
            System.out.println(csvParser.getHeaderMap());
        } catch (IOException e) {
            e.printStackTrace();
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
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
