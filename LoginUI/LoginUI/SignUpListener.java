package LoginUI;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SignUpListener implements ActionListener {

    private JFrame frame;
    private JTextField userNameField;
    private JTextField passwordField;

    public SignUpListener(JTextField usernameTextField, JTextField passwordTextField, JFrame frame) {
        this.userNameField = usernameTextField;
        this.passwordField = passwordTextField;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SignUpManager signUp = new SignUpManager();
        if (userNameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in credentials");

        }else if (signUp.usernameChecker(userNameField.getText())) {
            JOptionPane.showMessageDialog(frame, "An account with this userName already exists, choose another One.");

        } else if (userNameField.getText().contains(" ")) {
            JOptionPane.showMessageDialog(frame, "Username can only contain characters, Letters, and Numbers");
            
        }else if (signUp.passwordLength(passwordField.getText())) {
            JOptionPane.showMessageDialog(frame, "Password needs to be at least 6 characters Long.");

        } else {
            signUp.accountCreator(userNameField.getText(), passwordField.getText());
            JOptionPane.showMessageDialog(frame, "Account scuccesfully created.");

        }
    }

}
