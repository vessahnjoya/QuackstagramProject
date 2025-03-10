import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class handles saving of user information upon sign Up
 */
public class SignUpCredentials {

    private final String profilePhotoStoragePath = "img/storage/profile/";
    private final String credentialsFilePath = "data/credentials.txt";
/**
 * This method saves profile picture to txt file
 * @param file
 * @param username
 */
    private void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(profilePhotoStoragePath + username + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/**
 * This methods saves user information to txt file
 * @param username
 * @param password
 * @param bio
 */
    private void saveCredentials(String username, String password, String bio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/credentials.txt", true))) {
            AffineCipher passwordHasher = new AffineCipher(password);
            writer.write(username + ":" + passwordHasher.encrypt() + ":" + bio);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/**
 * This methods verifies whether a username already exists
 * @param username
 * @return boolean
 */
    private boolean doesUsernameExist(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ":")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
/**
 * This method calls profile picture saver which is private to hide implementation from other classes
 * @param file
 * @param username
 */
    public void savePFP(File file, String username) {
        saveProfilePicture(file, username);
    }
/**
 * This method calls credentials saver which is private to hide implementation from other class
 * @param username
 * @param password
 * @param bio
 */
    public void saveCreds(String username, String password, String bio) {
        saveCredentials(username, password, bio);
    }
/**
 * This method calls username verifier which is private to hide implementation from other classes
 * @param username
 * @return
 */
    public boolean userExistence(String username) {
        return doesUsernameExist(username);
    }
}