package New_Refactor_Sign_In;
import Refactor_UI.*;
import User.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class RefactoredSignIn extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;

    private User newUser;
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JButton btnSignIn;
    private JButton btnRegisterNow;
    private JLabel lblPhoto;
    private JLabel lblRegister;
    private JPanel fieldsPanel;
    private JPanel registerPanel;
    private JPanel headerPanel;
    private JPanel buttonPanel;
    private CredentialsVerifier credentialsVerifier;
    

    public RefactoredSignIn() {
        setTitle("Quackstagram - Register");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        credentialsVerifier = new CredentialsVerifier(); 
        // credentialsVerifier = new CredentialsVerifier();
        initializeUI();
    }

private void initializeUI(){
       registerPanel();
       textField();
       profilePicture();
       usernameField();
       passwordField();
       signInButton();
       registerButton();
       addComponents();
       headerPanel();
       buttonPanel();
}

    private Component headerPanel(){
        headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height

        headerPanel.add(registerPanel());

        return headerPanel;
    }

    private Component buttonPanel(){
        buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // Grid layout with 1 row, 2 columns
        buttonPanel.setBackground(Color.white);

        buttonPanel.add(signInButton());
        buttonPanel.add(registerButton());

        return buttonPanel;
    }

    private Component registerPanel(){
        lblRegister = new JLabel("Quackstagram 🐥");

        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white

        return lblRegister;
    }

    private Component textField(){
        fieldsPanel = new JPanel();

        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        return fieldsPanel;
    }

    private Component profilePicture(){
        lblPhoto = new JLabel();

        lblPhoto.setPreferredSize(new Dimension(80, 80));
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setVerticalAlignment(JLabel.CENTER);
        lblPhoto.setIcon(new ImageIcon(new ImageIcon("img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));

        JPanel photoPanel = new JPanel(); // Use a panel to center the photo label
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        photoPanel.add(lblPhoto);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);   

        return photoPanel;
    }

    private Component usernameField() {
        txtUsername = new JTextField("Username");
        txtUsername.setForeground(Color.BLACK);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);

        return txtUsername;
    }

    private Component passwordField(){
        txtPassword = new JTextField("Password");
        txtPassword.setForeground(Color.BLACK);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);

        return txtPassword;
    }

    private Component signInButton(){
        btnSignIn = new JButton("Sign-In");
        btnSignIn.addActionListener(this::onSignInClicked);

        btnSignIn.setForeground(Color.BLACK);
        btnSignIn.setBackground(new Color(255, 90, 95));

        btnSignIn.setFocusPainted(false);
        btnSignIn.setBorderPainted(false);

        btnSignIn.setFont(new Font("Arial", Font.BOLD, 15));

        registerPanel = new JPanel(new BorderLayout());
        registerPanel.setBackground(Color.BLACK);
        registerPanel.add(btnSignIn, BorderLayout.CENTER);

        return btnSignIn;
    }  

    private void addComponents(){               
        add(headerPanel(), BorderLayout.NORTH);
        add(fieldsPanel, BorderLayout.CENTER);
        add(registerPanel(), BorderLayout.SOUTH);
        add(buttonPanel(), BorderLayout.SOUTH);
    }
    
    private Component registerButton(){
        btnRegisterNow = new JButton("No Account? Register Now");
        btnRegisterNow.addActionListener(this::onRegisterNowClicked);

        btnRegisterNow.setBackground(Color.WHITE); // Set a different color for distinction
        btnRegisterNow.setForeground(Color.BLACK);

        btnRegisterNow.setFocusPainted(false);
        btnRegisterNow.setBorderPainted(false);

        return btnRegisterNow;
    }


   private void onSignInClicked(ActionEvent event) {
    String enteredUsername = txtUsername.getText();
    String enteredPassword = txtPassword.getText();
    System.out.println(enteredUsername + " <-> " + enteredPassword);
    newUser = credentialsVerifier.authenticate(enteredUsername, enteredPassword);
    if(newUser != null){
    System.out.println("It worked");
         // Close the SignUpUI frame
    dispose();

    // Open the SignInUI frame
    SwingUtilities.invokeLater(() -> {
        InstagramProfileUI profileUI = new InstagramProfileUI(newUser);
        profileUI.setVisible(true);
    });
    } else {
        System.out.println("It Didn't");
    }
}

private void onRegisterNowClicked(ActionEvent event) {
    // Close the SignInUI frame
    dispose();

    // Open the SignUpUI frame
    SwingUtilities.invokeLater(() -> {
        RefactoredSignUp signUpFrame = new RefactoredSignUp();
        signUpFrame.setVisible(true);
    });
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RefactoredSignIn frame = new RefactoredSignIn();
            frame.setVisible(true);
        });
    }
}
