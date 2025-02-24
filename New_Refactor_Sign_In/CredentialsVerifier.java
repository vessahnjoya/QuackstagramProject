package New_Refactor_Sign_In;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import User.*;
import Hasher.*;
public class CredentialsVerifier {
    private User verifyCredentials(String username, String password){
        try(BufferedReader reader  = new BufferedReader(new FileReader("data/credentials.txt"))){
            String line;
            AffineCipher passwordDecrypterCipher = new AffineCipher(password);
            while((line = reader.readLine()) != null){
                String[] credentials = line.split(":");
                if(credentials[0].equals(username) && credentials[1].equals(passwordDecrypterCipher.encrypt())){
                    String bio = credentials[2];

                    return new User(username, bio, password);

                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public User authenticate(String username, String password){
        return verifyCredentials(username, password);
    }
    
    public static void saveUserInformation(User user){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("data/users.txt", true))){
            writer.write(user.toString());
            writer.newLine();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
