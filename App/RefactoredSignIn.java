import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * This class handles Ui components related to sign In pages and some logic such
 * as calling of credentialsverifier class
 */
public class RefactoredSignIn extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;

    private final Color blueColor = new Color(51, 204, 255);
    private User newUser;
    private static User currentUser;
    private static JTextField txtUsername;
    private JTextField txtPassword;
    private JButton btnSignIn;
    private JButton btnRegisterNow;
    private JLabel lblRegister;
    private JPanel fieldsPanel;
    private JPanel headerPanel;
    private JPanel buttonPanel;
    private CredentialsVerifier credentialsVerifier;
    private static String loggedInUsername;

    /**
     * The constructor initializes default values for the frame
     */
    public RefactoredSignIn() {
        setTitle("Quackstagram - Register");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        credentialsVerifier = new CredentialsVerifier();
        initializeUI();
    }

    /**
     * This method initializes UI componentq and sets design data
     */
    private void initializeUI() {
        addComponents();
        Color backgroundColor = new Color(201, 189, 0);
        getContentPane().setBackground(backgroundColor); // Set the main frame's background
        fieldsPanel.setBackground(backgroundColor); // Set the fields panel background
        buttonPanel.setBackground(backgroundColor); // Set the button panel background
    }

    /**
     * This method creates a header panel to whoch is added the register Panel
     * 
     * @return header panel
     */
    private Component headerPanel() {
        headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height

        headerPanel.add(registerLabel());
        return headerPanel;
    }

    /**
     * This method creates a pannel containing sign In and sign Up buttons
     * 
     * @return button panel
     */
    private Component buttonPanel() {
        buttonPanel = new JPanel(new GridLayout(2, 1)); // Grid layout with 2 row, 1 columns
        buttonPanel.setBackground(Color.white);

        buttonPanel.add(signInButton(), BorderLayout.CENTER);
        buttonPanel.add(registerButton(), BorderLayout.SOUTH);

        return buttonPanel;
    }

    /**
     * This method creates a register label with App name
     * 
     * @return register label (lblRegister)
     */
    private Component registerLabel() {
        lblRegister = new JLabel("Quackstagram 🐥");

        lblRegister.setFont(new Font("Arial", Font.ITALIC, 20));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white

        return lblRegister;
    }

    /**
     * This method a
     * 
     * @return
     */
    private Component textField() {
        fieldsPanel = new JPanel();
        fieldsPanel.add(profilePicture());
        fieldsPanel.add(usernameField());
        fieldsPanel.add(passwordField());

        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        return fieldsPanel;
    }

    /**
     * This methods adds the dacs logo to a panel
     * 
     * @return photo panel
     */
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

    /**
     * This methods creates a field to input for username
     * 
     * @return username textfield(txtUsername)
     */
    private Component usernameField() {
        txtUsername = new JTextField("Username");
        txtUsername.setForeground(Color.BLACK);
        txtUsername.setPreferredSize(new Dimension(200, 30)); // Preferred size
        txtUsername.setMaximumSize(new Dimension(200, 30)); // Maximum size

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);

        return txtUsername;
    }

    /**
     * This methods creates a field to input for password
     * 
     * @return password field (txtPassword)
     */
    private Component passwordField() {
        txtPassword = new JTextField("Password");
        txtPassword.setForeground(Color.BLACK);
        txtPassword.setPreferredSize(new Dimension(200, 30)); // Preferred size
        txtPassword.setMaximumSize(new Dimension(200, 30)); // Maximum size

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);

        return txtPassword;
    }

    /**
     * This method is used to get the logged In user's username directly from the
     * username field
     * 
     * @return username
     */
    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

       /**
     * This method is used to get the logged In user's username directly from the
     * username field
     * 
     * @return username
     */
    public static User getLoggedInUser() {
        return currentUser;
    }
    /**
     * This methods create a sign In button and add an action listener to it
     * 
     * @return Sign In button
     */
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

    /**
     * This methods create a register button and add an action listener to it
     * 
     * @return register button
     */
    private Component registerButton() {
        btnRegisterNow = new JButton("No Account? Register Now");
        btnRegisterNow.addActionListener(this::onRegisterNowClicked);

        btnRegisterNow.setBackground(blueColor); // Set a different color for distinction
        btnRegisterNow.setForeground(Color.BLACK);

        btnRegisterNow.setFocusPainted(false);
        btnRegisterNow.setBorderPainted(false);

        return btnRegisterNow;
    }

    /**
     * This method adds Components to Frame
     */
    private void addComponents() {
        add(headerPanel(), BorderLayout.NORTH);
        add(textField(), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);
    }

    /**
     * This methods calls the credentials verfier class and verifies enterred
     * credentials and if correct open the profile UI page
     * 
     * @param event
     */
    private void onSignInClicked(ActionEvent event) {
        String enteredUsername = txtUsername.getText();
        String enteredPassword = txtPassword.getText();
        // debug statement
        System.out.println(enteredUsername + " <-> " + enteredPassword);
        newUser = credentialsVerifier.authenticate(enteredUsername, enteredPassword);

        if (newUser != null) {
            loggedInUsername = enteredUsername;
            currentUser = newUser;
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

    /**
     * This method opens sign Up page
     * 
     * @param event
     */
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
