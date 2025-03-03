
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class RefactoredSignUp extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private final Color blueColor = new Color(51, 204, 255);

    private JTextField txtUsername;
    private JTextField txtPassword;
    private JTextField txtBio;

    private JPanel headerPanel;
    private JPanel fieldsPanel;
    private JPanel buttonPanel;

    private JButton btnRegister;
    private JButton btnUploadPhoto;
    private JButton btnSignIn;

    private JLabel lblPhoto;
    private JLabel lblRegister;

    private User newUser;

    private SignUpCredentials saveProfilePicture = new SignUpCredentials();
    private SignUpCredentials saveCredentials = new SignUpCredentials();
    private SignUpCredentials doesUsernameExist = new SignUpCredentials();


    public RefactoredSignUp() {
        setTitle("Quackstagram - Register");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        initializeUI();
    }

    private void initializeUI() {
        addComponent();
    }

    private Component headerPanel() {
        headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height

        headerPanel.add(registerPanel());

        return headerPanel;
    }

    private Component registerPanel() {
        lblRegister = new JLabel("Quackstagram 🐥");

        lblRegister.setFont(new Font("Arial", Font.ITALIC, 20));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white

        return lblRegister;
    }

    private Component profilePicture() {
        lblPhoto = new JLabel();

        lblPhoto.setPreferredSize(new Dimension(80, 80));
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setVerticalAlignment(JLabel.CENTER);
        lblPhoto.setIcon(new ImageIcon(
                new ImageIcon("img/logos/DACS.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));

        JPanel photoPanel = new JPanel(); // Use a panel to center the photo label
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(lblPhoto);

        return photoPanel;
    }

    private Component textField() {
        fieldsPanel = new JPanel();

        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        fieldsPanel.add(profilePicture());
        fieldsPanel.add(usernameField());
        fieldsPanel.add(bioField());
        fieldsPanel.add(passwordField());
        fieldsPanel.add(uploadPhoto(), BorderLayout.CENTER);

        return fieldsPanel;
    }

    private Component usernameField() {
        txtUsername = new JTextField("Username");
        txtUsername.setForeground(Color.GRAY);

        return txtUsername;
    }

    private Component bioField() {
        txtBio = new JTextField("Bio");
        txtBio.setForeground(Color.GRAY);

        return txtBio;
    }

    private Component passwordField() {
        txtPassword = new JTextField("Password");
        txtPassword.setForeground(Color.GRAY);

        return txtPassword;
    }

    private Component uploadPhoto() {
        btnUploadPhoto = new JButton("Upload Photo");

        btnUploadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProfilePictureUpload();
            }
        });

        return btnUploadPhoto;
    }

    private Component registerButton() {
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(this::onRegisterClicked);

        btnRegister.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        btnRegister.setForeground(Color.BLACK); // Set the text color to black

        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);

        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));

        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);

        return btnRegister;
    }

    private Component signInButton() {
        btnSignIn = new JButton("Already have an account? Sign In");
        btnSignIn.setBackground(blueColor);
        btnSignIn.setFocusPainted(false);
        btnSignIn.setBorderPainted(false);

        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignInUI();
            }
        });

        return btnSignIn;
    }

    private Component buttonPanel() {
        buttonPanel = new JPanel(new GridLayout(2, 1)); // Grid layout with 2 row, 1 columns
        buttonPanel.setBackground(Color.white);

        buttonPanel.add(registerButton(), BorderLayout.CENTER);
        buttonPanel.add(signInButton(), BorderLayout.SOUTH);

        return buttonPanel;
    }

    private void addComponent() {
        add(headerPanel(), BorderLayout.NORTH);
        add(textField(), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);
    }

    private boolean onRegisterClicked(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String bio = txtBio.getText();

        if (doesUsernameExist.userExistence(username)) {
            showErrorMessage("Username already exists. Please choose a different username.");
            return false;
        }

        registerUser(username, password, bio);
        openSignInUI();
                return rootPaneCheckingEnabled;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void registerUser(String username, String password, String bio) {
        newUser = new User(username, bio, password);
        CredentialsVerifier.saveUserInformation(newUser);
        saveCredentials.saveCreds(username, password, bio);
        handleProfilePictureUpload();
        dispose();
    }

    // Method to handle profile picture upload
    private void handleProfilePictureUpload() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveProfilePicture.savePFP(selectedFile, txtUsername.getText());
        }
    }

    private void openSignInUI() {
        // Close the SignUpUI frame
        dispose();

        // Open the SignInUI frame
        SwingUtilities.invokeLater(() -> {
            RefactoredSignIn signInFrame = new RefactoredSignIn();
            signInFrame.setVisible(true);
        });
    }

}
