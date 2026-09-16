package smartcargosystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Database connection settings
    private static final String URL = "jdbc:mysql://localhost:3306/SmartCargoDB?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = "admin123"; 

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Register MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found! Make sure JDBC JAR is added.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database Connection Failed! Check if XAMPP MySQL is running.");
            e.printStackTrace();
        }
        return conn;
    }

    // Main test method to instantly verify connection
    public static void main(String[] args) {
        Connection testConn = getConnection();
        if (testConn != null) {
            System.out.println("SUCCESS: Connected to SmartCargoDB!");
        } else {
            System.out.println("FAILED: Could not connect to the database.");
        }
    }
}