import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SignUpManager {

    public boolean accountChecker(String userName, String password) {
        try {
            FileReader fileReader = new FileReader("data/users.txt");
            try (Scanner scannerReader = new Scanner(fileReader)) {
                while (scannerReader.hasNextLine()) {
                    String[] credentials = scannerReader.nextLine().split(",", 2);
                    if (credentials.length == 2 && credentials[0].equalsIgnoreCase(userName)
                            && credentials[1].equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
        return false;
    }

    public boolean usernameChecker(String userName) {
        try {
            FileReader fileReader = new FileReader("data/users.txt");
            try (Scanner scannerReader = new Scanner(fileReader)) {
                while (scannerReader.hasNextLine()) {
                    String[] credentials = scannerReader.nextLine().split(",", 2);
                    for (int i = 0; i < credentials.length; i++) {
                        if (credentials[i].equalsIgnoreCase(userName)) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
        return false;

    }

    public void accountCreator(String userName, String password) {
        try (FileWriter fileWriter = new FileWriter("data/users.txt", true)) {
            fileWriter.write(userName + "," + password + System.getProperty("line.separator"));
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }
}