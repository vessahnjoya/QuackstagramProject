import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SignUpCredentials{

    private final String profilePhotoStoragePath = "img/storage/profile/";
    private final String credentialsFilePath = "data/credentials.txt";

    private void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(profilePhotoStoragePath + username + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCredentials(String username, String password, String bio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/credentials.txt", true))) {
            AffineCipher passwordHasher = new AffineCipher(password);
            writer.write(username + ":" + passwordHasher.encrypt() + ":" + bio);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void savePFP(File file, String username){
        saveProfilePicture(file, username);
    }
    public void saveCreds(String username, String password, String bio){
        saveCredentials(username, password, bio);
    }
    public boolean userExistence(String username){
        return doesUsernameExist(username);
    }
}