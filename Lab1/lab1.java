import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class lab1{
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Log In");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel userNameLabel = new JLabel("UserName");
        JTextField usernameField = new JTextField(10);
        JLabel passwordLabel = new JLabel("PassWord");
        JTextField passwordField = new JTextField(10);

        JButton signInButton = new JButton("Sign In");
        JButton signUpButton = new JButton("Sign Up");

        ActionListener signInListener = new SignInListener(usernameField.getText(), passwordField.getText(), frame);
        signInButton.addActionListener(signInListener);

        JPanel signInPanel = new JPanel();
        signInPanel.setLayout(new GridLayout(4,1));

        signInPanel.add(userNameLabel);
        signInPanel.add(usernameField);
        signInPanel.add(passwordLabel);
        signInPanel.add(passwordField);
        signInPanel.add(signInButton);
        signInPanel.add(signUpButton);

        

        frame.add(signInPanel);
        frame.setVisible(true);





    }


}