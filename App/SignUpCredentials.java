import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
            path = profilePhotoStoragePath + username + "/" + ".png";
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
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(query)) {
            AffineCipher passwordHasher = new AffineCipher(user.getPassword());

            statement.setString(1, user.getUsername());
            statement.setString(2, passwordHasher.encrypt());
            statement.setString(3, user.getBio());
            statement.setString(4, pfpPath);
            statement.executeUpdate();
            // debug statement
            System.out.println("saving worked");
            connection.close();
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
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            var result = statement.executeQuery();
            connection.close();
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

    /**
     * This method is used to call the user info saver, preventing unwanted access
     * to the method
     * 
     * @param user
     * @param pfpPath
     */
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