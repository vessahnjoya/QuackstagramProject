import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides method to authenticate users and verify credentials
 */
public class CredentialsVerifier {

    /**
     * This verifies given user data against stored credentials
     * 
     * @param username The username provided by the user
     * @param password The password provided by the user
     * @return A User object if authentication is successful, otherwise null
     */
    private User verifyCredentials(String username, String password) {

        String query = "SELECT * FROM users WHERE username = ? AND user_password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String bio = getBio(username);
                // debug statement
                System.out.println(" login worked");
                return new User(username, bio, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Authenticates a user by verifying the provided username and password
     * 
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return A User object if authentication is successful, otherwise null
     */
    public User authenticate(String username, String password) {
        return verifyCredentials(username, password);
    }

    /**
     * saves user information to database
     * 
     * @param user The object containing user details
     */
    public static void saveUserInformation(User user, String pfpPath) {

        String query = "INSERT INTO users (username, user_password, bio, profile_picture_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getBio());
            stmt.setString(4, pfpPath);
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected);
            // debug statement
            System.out.println("saving worked");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getBio(String username) {
        String query = "SELECT bio FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String bio = rs.getString("bio");
                System.out.println("Retrieved bio for " + username + ": " + bio);
                return bio;
            }
        } catch (SQLException e) {
            System.err.println("Failed to get bio for username: " + username + ", Error: " + e.getMessage());
        }
        return null;
    }
}
