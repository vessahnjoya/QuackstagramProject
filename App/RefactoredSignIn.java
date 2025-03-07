
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class RefactoredSignIn extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;

    private final Color blueColor = new Color(51, 204, 255);
    private User newUser;
    private static JTextField txtUsername;
    private JTextField txtPassword;
    private JButton btnSignIn;
    private JButton btnRegisterNow;
    private JLabel lblRegister;
    private JPanel fieldsPanel;
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
        initializeUI();
    }

    private void initializeUI() {
        addComponents();
        Color backgroundColor = new Color(201, 189, 0);
        getContentPane().setBackground(backgroundColor); // Set the main frame's background
        fieldsPanel.setBackground(backgroundColor); // Set the fields panel background
        buttonPanel.setBackground(backgroundColor); // Set the button panel background
    }

    private Component headerPanel() {
        headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height

        headerPanel.add(registerPanel());
        return headerPanel;
    }

    private Component buttonPanel() {
        buttonPanel = new JPanel(new GridLayout(2, 1)); // Grid layout with 2 row, 1 columns
        buttonPanel.setBackground(Color.white);

        buttonPanel.add(signInButton(), BorderLayout.CENTER);
        buttonPanel.add(registerButton(), BorderLayout.SOUTH);

        return buttonPanel;
    }

    private Component registerPanel() {
        lblRegister = new JLabel("Quackstagram 🐥");

        lblRegister.setFont(new Font("Arial", Font.ITALIC, 20));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white

        return lblRegister;
    }

    private Component textField() {
        fieldsPanel = new JPanel();
        fieldsPanel.add(profilePicture());
        fieldsPanel.add(usernameField());
        fieldsPanel.add(passwordField());

        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        return fieldsPanel;
    }

    private Component profilePicture() {
        JLabel lblPhoto = new JLabel();

        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setVerticalAlignment(JLabel.CENTER);

        Image scaledImage = new ImageIcon("img/logos/DACS.png").getImage()
                .getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        lblPhoto.setIcon(new ImageIcon(scaledImage));

        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.setBackground(new Color(201, 189, 0));
        photoPanel.setOpaque(true);

        photoPanel.add(lblPhoto, BorderLayout.CENTER);

        return photoPanel;
    }


    private Component usernameField() {
        txtUsername = new JTextField("Username");
        txtUsername.setForeground(Color.BLACK);
        txtUsername.setPreferredSize(new Dimension(200, 30)); // Preferred size
        txtUsername.setMaximumSize(new Dimension(200, 30)); // Maximum size

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);

        return txtUsername;
    }

    private Component passwordField() {
        txtPassword = new JTextField("Password");
        txtPassword.setForeground(Color.BLACK);
        txtPassword.setPreferredSize(new Dimension(200, 30)); // Preferred size
        txtPassword.setMaximumSize(new Dimension(200, 30)); // Maximum size

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);

        return txtPassword;
    }


    public static String getLoggedInUsername() {
        return txtUsername.getText().trim();
    }

    private Component signInButton() {
        btnSignIn = new JButton("Sign-In");
        btnSignIn.addActionListener(this::onSignInClicked);

        btnSignIn.setForeground(Color.BLACK);
        btnSignIn.setBackground(new Color(255, 90, 95));

        btnSignIn.setFocusPainted(false);
        btnSignIn.setBorderPainted(false);

        btnSignIn.setFont(new Font("Arial", Font.BOLD, 15));

        return btnSignIn;
    }

    private Component registerButton() {
        btnRegisterNow = new JButton("No Account? Register Now");
        btnRegisterNow.addActionListener(this::onRegisterNowClicked);

        btnRegisterNow.setBackground(blueColor); // Set a different color for distinction
        btnRegisterNow.setForeground(Color.BLACK);

        btnRegisterNow.setFocusPainted(false);
        btnRegisterNow.setBorderPainted(false);

        return btnRegisterNow;
    }

    private void addComponents() {
        add(headerPanel(), BorderLayout.NORTH);
        add(textField(), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);
    }

    private void onSignInClicked(ActionEvent event) {
        String enteredUsername = txtUsername.getText();
        String enteredPassword = txtPassword.getText();
        System.out.println(enteredUsername + " <-> " + enteredPassword);
        newUser = credentialsVerifier.authenticate(enteredUsername, enteredPassword);

        if (newUser != null) {
            JOptionPane.showMessageDialog(this, "Login Succesful, Welcome!");
            // Close the SignUpUI frame
            dispose();

            // Open the SignInUI frame
            SwingUtilities.invokeLater(() -> {
                InstagramProfileUI profileUI = new InstagramProfileUI(newUser);
                profileUI.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, "Please verify your credentials");
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
}
