import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SignUpManager {

    public boolean accountChecker(String userName, String password) {
        try {
            FileReader fileReader = new FileReader("Users.txt");
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

    public boolean accountChecker(String userName) {
        try {
            FileReader fileReader = new FileReader("Users.txt");
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
        try (FileWriter fileWriter = new FileWriter("Users.txt", true)) {
            fileWriter.write(userName + "," + password + System.getProperty("line.separator"));
            System.out.println("Data successfully written to file");
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }
}