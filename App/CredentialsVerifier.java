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
        AffineCipher passwordHasher = new AffineCipher(password);

        String query = "SELECT * FROM users WHERE username = ? AND user_password = ?";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, passwordHasher.encrypt());
            var rs = statement.executeQuery();
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

    private String getBio(String username) {
        String query = "SELECT bio FROM users WHERE username = ?";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            var rs = statement.executeQuery();
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
