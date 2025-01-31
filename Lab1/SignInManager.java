import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SignInManager {

      public boolean login(String userName, String password){

        try{
            FileReader fileReader = new FileReader("C:\\Users\\njoya\\Desktop\\OOM\\Lab1\\Users.txt");
            try (Scanner scannerReader = new Scanner(fileReader)) {
                while(scannerReader.hasNextLine()){ 
                    String[] credentials = scannerReader.nextLine().split(" ", 2);
                    if(credentials.length == 2 && credentials[0].equalsIgnoreCase(userName) && credentials[1].equals(password)){
                        return true;
                    }
                    
                }
            }
        }catch(IOException error){
            System.out.println(error.getMessage());
        }
                return false;
    }
    
}
