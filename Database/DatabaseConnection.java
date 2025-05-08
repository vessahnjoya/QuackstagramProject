package Database;

import java.sql.*;

public class DatabaseConnection {

    private static String dbUrl = "jdbc:mysql://localhost:3306/Quackstagram";
    private static String username = "Yurpi";
    private static String password = "senior_Rascar";

    private void createDataBase() {
        try (Connection conn = DriverManager.getConnection(dbUrl, username, password)) {
            System.out.println("done");
        } catch (Exception e) {
            System.out.println("failed:" + e.getMessage());;
        }
    }

    public static void main(String[] args) {
        DatabaseConnection dbconnection = new DatabaseConnection();
        dbconnection.createDataBase();
    }
}