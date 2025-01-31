
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class SignInListener implements ActionListener {

    private JFrame frame;
    private String userName;
    private String password;
    public SignInListener(String userName, String password,JFrame frame){
        this.userName = userName;
        this.password = password;
        this.frame = frame;


    }
    @Override
    public void actionPerformed(ActionEvent e) {
       
        SignInManager loginManager = new SignInManager();
        if(loginManager.login(userName, password)){
            JOptionPane.showMessageDialog(frame, "LogIn Succesfull");
        }else{
            JOptionPane.showMessageDialog(frame, "Wrrong UserName and password Combinations, Retry!");
        }

    }

  
    
}
