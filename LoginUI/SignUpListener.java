import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SignUpListener implements ActionListener {
    
    private JFrame frame;
    private JTextField userNameField;
    private JTextField passwordField;
    public SignUpListener(JTextField usernameTextField, JTextField passwordTextField,JFrame frame){
        this.userNameField = usernameTextField;
        this.passwordField = passwordTextField;
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       SignUpManager signUp = new SignUpManager();
       if(signUp.accountChecker(userNameField.getText(), passwordField.getText())){
        JOptionPane.showMessageDialog(frame, "Account witht these already exists, please LogIn");
       }else if(signUp.accountChecker(userNameField.getText())){
        JOptionPane.showMessageDialog(frame, "Sorry an account with this userName already exists, choose another One");
       }else{
        signUp.accountCreator(userNameField.getText(), passwordField.getText());
        JOptionPane.showMessageDialog(frame, "Account scuccesfully created");

       }
    }

}
