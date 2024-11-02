package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    public void loadData(String fileName, int tableName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             CSVParser csvParser = new CSVParser(inputStreamReader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            if (inputStream == null) {
                throw new FileNotFoundException("File not found: " + fileName);
            }

           var headerMap = csvParser.getHeaderMap();
           StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS placeholder (");
           StringBuilder insertDataSQL = new StringBuilder("INSERT INTO placeholder (");

           int i = 0;
            for(String header: headerMap.keySet()){
                switch (i) {
                    case 0:
                        createTableSQL.append(header + " TEXT PRIMARY KEY");
                        insertDataSQL.append(header);
                        break;
                    default:
                        createTableSQL.append(", " + header + " TEXT");
                        insertDataSQL.append(", " + header);
                        break;
                }
                i++;
            }

            createTableSQL.append(");");
            Statement statement = this.connection.createStatement();
            statement.execute(createTableSQL.toString());


            insertDataSQL.append(") VALUES (");

            for (int k = 0; k < headerMap.size(); k++) {
                if (k > 0) {
                    insertDataSQL.append(", ");
                }
                insertDataSQL.append("?");
            }
            insertDataSQL.append(")");

            PreparedStatement preparedStatement = this.connection.prepareStatement(insertDataSQL.toString());
            connection.setAutoCommit(false);

            for(CSVRecord record: csvParser){
                int headerIndex = 1;
                for (String header : headerMap.keySet()) {
                   String temp = record.get(header);
                   preparedStatement.setString(headerIndex, temp);
                   headerIndex++;
                }
                preparedStatement.addBatch();
            }

            int[] rows = preparedStatement.executeBatch();
            connection.commit();

            System.out.println(rows.length + " rows loaded into your database!"); 


        } catch (IOException | SQLException e) {
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

    public void executeQuery(String sql) {
        ResultSet resultSet = null;
        Statement stmt = null;

        System.out.println("");
        
        try {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(sql);
    
            int columnCount = resultSet.getMetaData().getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", resultSet.getMetaData().getColumnName(i));
            }
            System.out.println();
    
            for (int i = 0; i < columnCount; i++) {
                System.out.print("--------------------");
            }
            System.out.println(); 
    
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    System.out.printf("%-20s", value); 
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
        }
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
