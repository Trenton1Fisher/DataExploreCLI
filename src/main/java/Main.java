package main.java;

import java.sql.SQLException;
import java.util.Scanner;
//import main.java.SQLiteManager;

public class Main {
    //private static SQLiteManager sqliteManager;
    private static Scanner scanner;
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        
        System.out.println("");
        System.out.println("Welcome to the CSV to SQLite Data Visualizer!");
        try {
            App();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } finally {
            scanner.close();
            //sqliteManager.closeConnection();
        }
    }

    private static void App() throws SQLException {
        while(true){
            System.out.println("\nMain Menu:");
            System.out.println("1. Load a CSV file into the database");
            System.out.println("2. View data");
            System.out.println("3. Manipulate data");
            System.out.println("4. Exit");
            System.out.print("Select an option (1-4): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            System.out.println("");

            switch (choice) {
                case 1:
                    System.out.println("Please choose which dataset you would like to load");
                    System.out.println("1. IMDB Dataset");
                    System.out.println("2. Top spotify streamed songs");
                    System.out.println("3. Books of the Decade");
                    int fileChoice = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Great Choice! Now please provide a name for the database to load data");
                    String dbName = scanner.nextLine();
                    System.out.println(fileChoice + " " + dbName);
                    System.out.println("");
                    break;
                case 2: 
                    System.out.println("View all data in chunks");
                    break;
                case 3: 
                    System.out.println("Run queries and manipulate data");
                    break;
                case 4: 
                    System.out.println("");
                    System.out.println("Exiting the game. Goodbye!");
                    return;  
                default:
                    break;
            }
        }
    }
}