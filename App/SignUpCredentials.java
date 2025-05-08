import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

/**
 * This class handles saving of user information upon sign Up
 */
public class SignUpCredentials {

    private final String profilePhotoStoragePath = "img/storage/profile/";
    private String path;

    /**
     * This method saves profile picture to txt file
     * 
     * @param file
     * @param username
     */
    private void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(profilePhotoStoragePath + username + ".png");
            path = outputFile.getPath().toString();
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * saves user information to database
     * 
     * @param user The object containing user details
     */
    private void saveUserInformation(User user, String pfpPath) {

        String query = "INSERT INTO users (username, user_password, bio, profile_picture_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            AffineCipher passwordHasher = new AffineCipher(user.getPassword());
            stmt.setString(1, user.getUsername());
            stmt.setString(2, passwordHasher.encrypt());
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

    /**
     * This methods verifies whether a username already exists
     * 
     * @param username
     * @return boolean
     */
    private boolean doesUsernameExist(String username) {
        String query = "SELECT * FROM users where username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ;
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            System.out.println("Failed " + e.getMessage());
        }

        return false;
    }

    /**
     * This method calls profile picture saver which is private to hide
     * implementation from other classes
     * 
     * @param file
     * @param username
     */
    public void savePFP(File file, String username) {
        saveProfilePicture(file, username);
    }

    public void saveUserInfo(User user, String pfpPath) {
        saveUserInformation(user, path);
    }

    /**
     * This method calls username verifier which is private to hide implementation
     * from other classes
     * 
     * @param username
     * @return
     */
    public boolean userExistence(String username) {
        return doesUsernameExist(username);
    }

    public String getPath() {
        return path;
    }
}