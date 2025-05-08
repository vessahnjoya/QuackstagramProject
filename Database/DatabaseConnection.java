package Database;

import java.sql.*;

/**
 * This class hanles the database connection and contains credentials to login
 * into the database and a test method to
 * ensure the database is accessible
 */
public class DatabaseConnection {

    private static String dbUrl = "jdbc:mysql://localhost:3306/Quackstagram";
    private static String username = "Yurpi";
    private static String password = "senior_Rascar";

    /**
     * This method creates the database connection
     */
    private void createDataBase() {
        try (Connection conn = DriverManager.getConnection(dbUrl, username, password)) {

        } catch (Exception e) {
            System.out.println("failed:" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseConnection dbconnection = new DatabaseConnection();
        dbconnection.createDataBase();
    }

    /**
     * Test Method
     */
    // private void TestDbconnection(){
    // try (Connection conn = DriverManager.getConnection(dbUrl, username,
    // password)) {
    // Statement stmt = conn.createStatement();
    // ResultSet resultSet = stmt.executeQuery(
    // "SELECT * FROM users"
    // );
    // while (resultSet.next()) {
    // System.out.println(resultSet.getString("username"));
    // System.out.println(resultSet.getString("user_password"));
    // }
    // } catch (Exception e) {
    // System.out.println("failed:" + e.getMessage());
    // }
    // }
}