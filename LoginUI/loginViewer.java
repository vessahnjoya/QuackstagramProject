package LoginUI;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class loginViewer{
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Log In");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField usernameField = new JTextField("Enter Your UserName",10);
        JTextField passwordField = new JTextField("Fill in your password",10);

        JButton signInButton = new JButton("Sign In");
        JButton signUpButton = new JButton("Sign Up");

        ActionListener signInListener = new SignInListener(usernameField, passwordField, frame);
        signInButton.addActionListener(signInListener);

        ActionListener signUpListener = new SignUpListener(usernameField, passwordField, frame);
        signUpButton.addActionListener(signUpListener);

        JPanel signInPanel = new JPanel();
        signInPanel.setLayout(new GridLayout(4,1));

        signInPanel.add(usernameField);
        signInPanel.add(passwordField);
        signInPanel.add(signInButton);
        signInPanel.add(signUpButton);

        

        frame.add(signInPanel);
        frame.setVisible(true);





    }


}