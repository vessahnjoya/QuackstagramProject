import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * 
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Quackstagram";
    private static final String USERNAME = "njoya";
    private static final String PASSWORD = "senior_Rascar";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}