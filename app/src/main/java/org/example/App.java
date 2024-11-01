package org.example;

import java.sql.SQLException;
import java.util.Scanner;
import org.example.SQLiteManager;

public class App {
    private static SQLiteManager sqliteManager;
    private static Scanner scanner;
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        
        System.out.println("");
        System.out.println("Welcome to the CSV to SQLite Data Visualizer!");
        try {
            runApp();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } finally {
            scanner.close();
           sqliteManager.close();
        }
    }

    private static void runApp() throws SQLException {
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Load a new CSV file into the database");
            System.out.println("2. View data You loaded");
            System.out.println("3. Run your own SQLite query");
            System.out.println("4. Exit");
            System.out.print("Select an option (1-4): ");
            
            int choice = getValidIntegerInput(1, 4);

            switch (choice) {
                case 1:
                    loadCSVIntoDatabase();
                    break;
                case 2:
                    viewData();
                    break;
                case 3:
                    manipulateData();
                    break;
                case 4:
                    System.out.println("Exiting the game. Goodbye!");
                    return;  
                default:
                    break;
            }
        }
    }

    private static void loadCSVIntoDatabase() throws SQLException {

        if (sqliteManager != null) {
            sqliteManager.close(); 
        }

        System.out.println("Please choose which dataset you would like to load");
        System.out.println("1. IMDB Dataset");
        System.out.println("2. Top Spotify Streamed Songs");
        System.out.println("3. Books of the Decade");

        int fileChoice = getValidIntegerInput(1, 3);
        String csvFileName = getCSVFilePath(fileChoice);

        System.out.println("Great choice! Now please provide a name for the database to load data:");
        String dbName = scanner.nextLine().trim();
        
        sqliteManager = new SQLiteManager("jdbc:sqlite:" + dbName + ".db");
        sqliteManager.connect(); 
        sqliteManager.loadData(csvFileName, fileChoice);
        System.out.println("Data loaded successfully into " + dbName + ".db");
    }

    private static String getCSVFilePath(int fileChoice) {
        switch (fileChoice) {
            case 1: return "imdb.csv";
            case 2: return "spotify.csv";
            case 3: return "books.csv";
            default: return null; 
        }
    }

    private static void viewData() {
        System.out.println("View all data in chunks");

    }

    private static void manipulateData() {
        System.out.println("Run queries and manipulate data");

    }

    private static int getValidIntegerInput(int min, int max) {
        int choice;
        while (true) {
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.print("Please select a valid option (" + min + "-" + max + "): ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number (" + min + "-" + max + "): ");
            }
        }
    }

}
