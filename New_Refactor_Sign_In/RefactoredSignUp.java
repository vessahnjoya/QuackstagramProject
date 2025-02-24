package New_Refactor_Sign_In;
import Hasher.*;
import User.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;



public class RefactoredSignUp extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;

    private JTextField txtUsername;
    private JTextField txtPassword;
    private JTextField txtBio;

    private JPanel headerPanel;
    private JPanel registerPanel;
    private JPanel fieldsPanel;
    private JPanel photoUploadPanel;

    private JButton btnRegister;
    private JButton btnUploadPhoto;
    private JButton btnSignIn;

    private JLabel lblPhoto;
    private JLabel lblRegister;

    private User newUser;
    
    private final String credentialsFilePath = "data/credentials.txt";
    private final String profilePhotoStoragePath = "img/storage/profile/";


    public RefactoredSignUp() {
        setTitle("Quackstagram - Register");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        initializeUI();
    }

    private void initializeUI(){
        registerPanel();
        textField();
        headerPanel();

        profilePicture();

        usernameField();
        bioField();
        passwordField();

        uploadPhoto();

        registerButton();
        signInButton();
        
        addComponent();
    }

    private Component headerPanel(){
        headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height

        headerPanel.add(registerPanel());

        return headerPanel;
    }

    private Component registerPanel(){
        lblRegister = new JLabel("Quackstagram 🐥");

        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white

        return lblRegister;
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

    private Component textField(){
        fieldsPanel = new JPanel();

        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        return fieldsPanel;
    }

    private Component usernameField(){
        txtUsername = new JTextField("Username");

        txtUsername.setForeground(Color.GRAY);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);

        return txtUsername;
    }

    private Component bioField(){
        txtBio = new JTextField("Bio");
        txtBio.setForeground(Color.GRAY);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtBio);

        return txtBio;
    }

    private Component passwordField(){
        txtPassword = new JTextField("Password");
        txtPassword.setForeground(Color.GRAY);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);

        return txtPassword;
    }

    private Component uploadPhoto(){
        btnUploadPhoto = new JButton("Upload Photo");

        photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        photoUploadPanel.add(btnUploadPhoto);
        fieldsPanel.add(photoUploadPanel);

        btnUploadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProfilePictureUpload();
            }
        });

        return btnUploadPhoto;
    } 
    
    private Component registerButton(){
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(this::onRegisterClicked);

        btnRegister.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        btnRegister.setForeground(Color.BLACK); // Set the text color to black

        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);

        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));

        registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button

        registerPanel.setBackground(Color.BLACK); // Background for the panel
        registerPanel.add(btnRegister, BorderLayout.CENTER);

        return btnRegister;
    }

    private Component signInButton(){
        btnSignIn = new JButton("Already have an account? Sign In");
    
        registerPanel.add(btnSignIn, BorderLayout.SOUTH);

        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignInUI();
            }
        });

        return btnSignIn;
    }

    private void addComponent(){
        add(headerPanel(), BorderLayout.NORTH);
        add(fieldsPanel, BorderLayout.CENTER);
        add(registerPanel, BorderLayout.SOUTH);
    }
    
    
    private void onRegisterClicked(ActionEvent event){
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String bio = txtBio.getText();

        if(doesUsernameExist(username)){
            showErrorMessage("Username already exists. Please choose a different username.");
            return;
        }

        registerUser(username, password, bio);
        openSignInUI();
    } 

    private void showErrorMessage(String message){
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void registerUser(String username, String password, String bio){
        newUser = new User(username, bio, password);
        CredentialsVerifier.saveUserInformation(newUser);
        saveCredentials(username, password, bio);
        handleProfilePictureUpload();
        dispose();
    }
    
    private boolean doesUsernameExist(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ":")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

     // Method to handle profile picture upload
     private void handleProfilePictureUpload() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveProfilePicture(selectedFile, txtUsername.getText());
        }
    }

    private void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(profilePhotoStoragePath + username + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveCredentials(String username, String password, String bio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/credentials.txt", true))) {
            AffineCipher passwordHasher = new AffineCipher(password);
            writer.write(username + ":" + passwordHasher.encrypt() + ":" + bio);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
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
