import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class provides method to authenticate user,s and verify credentials
 */
public class CredentialsVerifier {
     
    /**
     * This verifies given user data against stored credentials
     * @param username The username provided by the user
     * @param password The password provided by the user
     * @return A User object if authentication is successful, otherwise null
     */
    private User verifyCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/credentials.txt"))) {
            String line;
            AffineCipher passwordDecrypterCipher = new AffineCipher(password);
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials[0].equals(username) && credentials[1].equals(passwordDecrypterCipher.encrypt())) {
                    String bio = credentials[2];

                    return new User(username, bio, password);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Authenticates a user by verifying the provided username and password
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return A User object if authentication is successful, otherwise null
     */
    public User authenticate(String username, String password) {
        return verifyCredentials(username, password);
    }
/**
 * saves user information to txt file
 * @param user The object conataining user details
 */
    public static void saveUserInformation(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/users.txt", true))) {
            writer.write(user.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
