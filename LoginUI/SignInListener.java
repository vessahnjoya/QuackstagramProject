

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;



public class SignInListener implements ActionListener {

    private JFrame frame;
    private JTextField userNameField;
    private JTextField passwordField;
    public SignInListener(JTextField usernameTextField, JTextField passwordTextField,JFrame frame){
        this.userNameField = usernameTextField;
        this.passwordField = passwordTextField;
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       
        SignInManager signIn = new SignInManager();
        if(signIn.login(userNameField.getText(), passwordField.getText())){
            JOptionPane.showMessageDialog(frame, "LogIn Succesfull");
        }else{
            JOptionPane.showMessageDialog(frame, "Wrrong UserName or password, Retry!");
        }

    }

  
    
}
