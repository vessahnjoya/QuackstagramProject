
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
    private JButton btnSignIn;

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

    private Component registerPanel() {
        lblRegister = new JLabel("Quackstagram 🐥");

        lblRegister.setFont(new Font("Arial", Font.ITALIC, 20));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white

        return lblRegister;
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

    private Component textField() {
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        fieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        fieldsPanel.setBackground(new Color(201, 189, 0)); // Add this line to set the yellow background

        fieldsPanel.add(Box.createVerticalGlue());
        fieldsPanel.add(profilePicture());
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(usernameField());
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(bioField());
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(passwordField());
        fieldsPanel.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setBackground(new Color(201, 189, 0)); // Ensure button panel matches the yellow
        buttonPanel.add(uploadPhoto());
        fieldsPanel.add(buttonPanel);

        fieldsPanel.add(Box.createVerticalGlue());

        return fieldsPanel;
    }

    private Component usernameField() {
        txtUsername = new JTextField("Username");
        txtUsername.setForeground(Color.GRAY);
        txtUsername.setPreferredSize(new Dimension(200, 30)); // Preferred size
        txtUsername.setMaximumSize(new Dimension(200, 30)); // Maximum size
        return txtUsername;
    }

    private Component passwordField() {
        txtPassword = new JTextField("Password");
        txtPassword.setForeground(Color.GRAY);
        txtPassword.setPreferredSize(new Dimension(200, 30)); // Preferred size
        txtPassword.setMaximumSize(new Dimension(200, 30)); // Maximum size
        return txtPassword;
    }

    private Component bioField() {
        txtBio = new JTextField("Bio");
        txtBio.setForeground(Color.GRAY);
        txtBio.setPreferredSize(new Dimension(200, 30)); // Preferred size
        txtBio.setMaximumSize(new Dimension(200, 30)); // Maximum size
        return txtBio;
    }

    private Component uploadPhoto() {
        JButton btnUploadPhoto = new JButton("Upload Photo");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setBackground(new Color(201, 189, 0)); // Match yellow color
        buttonPanel.setOpaque(true);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder());

        buttonPanel.add(btnUploadPhoto);
        btnUploadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProfilePictureUpload();
            }
        });
        // Add revalidate and repaint to ensure the color is updated
        buttonPanel.revalidate();
        buttonPanel.repaint();

        return buttonPanel;
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
        // handleProfilePictureUpload();
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
