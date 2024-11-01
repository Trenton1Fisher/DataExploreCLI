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

    public void loadData(String fileName, int tableName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             CSVParser csvParser = new CSVParser(inputStreamReader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            if (inputStream == null) {
                throw new FileNotFoundException("File not found: " + fileName);
            }

           var headerMap = csvParser.getHeaderMap();
           StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS generic (");

           int i = 0;
            for(String header: headerMap.keySet()){
                switch (i) {
                    case 0:
                        createTableSQL.append(header + " TEXT PRIMARY KEY");
                        break;
                    default:
                        createTableSQL.append(", " + header + " TEXT");
                        break;
                }
                i++;
            }
            createTableSQL.append(");");
            System.out.println(createTableSQL);
            Statement statement = this.connection.createStatement();
            statement.execute(createTableSQL.toString());

            StringBuilder insertDataSQL = new StringBuilder("INSERT INTO generic ");

            int j = 0;
            insertDataSQL.append("(");
            for(String header: headerMap.keySet()) {
                switch (j) {
                    case 0:
                        insertDataSQL.append(header);
                        break;
                    default:
                        insertDataSQL.append(", " + header);
                        break;
                }
                j++;
            }
            insertDataSQL.append(") VALUES");
            for(CSVRecord record: csvParser){
                insertDataSQL.append("(");
                for(String header: headerMap.keySet()){
                    insertDataSQL.append("'" + record.get(header)+ "', ");
                }
                insertDataSQL.append("),");
            }
            insertDataSQL.append(");");
            System.out.println(insertDataSQL);

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
