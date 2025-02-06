import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class loginViewer {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        // frame.getContentPane().setBackground(Color.black);
        frame.setTitle("Quackstagram-Login");
        frame.setSize(300, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        JLabel welcomeLabel = new JLabel("Quackstagram");
        welcomeLabel.setFont(new Font("Italic", Font.ITALIC, 18));
        welcomeLabel.setForeground(Color.WHITE);

        JLabel lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(100, 100));
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setVerticalAlignment(JLabel.CENTER);
        lblPhoto.setIcon(new ImageIcon(new ImageIcon("img/logos/DACS.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

        // create fields and delete pre-existing content
        JTextField usernameField = new JTextField("UserName");
        usernameField.setBackground(Color.BLACK);
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.addMouseListener(new FieldsListener(usernameField));

        JTextField passwordField = new JTextField("Password");
        passwordField.setBackground(Color.BLACK);
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.addMouseListener(new FieldsListener(passwordField));

        JButton signInButton = new JButton("Sign In");
        signInButton.setBackground(new Color(1, 133, 255));
        signInButton.setFont(new Font("Albertus Extra Bold", Font.BOLD, 16));
        JButton signUpButton = new JButton("No account? Click Here");
        signUpButton.setBackground(new Color(255, 255, 255));

        ActionListener signInListener = new SignInListener(usernameField, passwordField, frame);
        signInButton.addActionListener(signInListener);
        ActionListener signUpListener = new SignUpListener(usernameField, passwordField, frame);
        signUpButton.addActionListener(signUpListener);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(Color.BLACK);
        welcomePanel.setLayout(new FlowLayout());
        welcomePanel.add(welcomeLabel);

        JPanel photoPanel = new JPanel();
        photoPanel.setBackground(Color.BLACK);
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(lblPhoto);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setBackground(Color.BLACK);
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(new EmptyBorder(15, 30, 15, 30));

        fieldsPanel.add(Box.createVerticalStrut(20));
        fieldsPanel.add(photoPanel);
        fieldsPanel.add(Box.createVerticalStrut(20));
        fieldsPanel.add(usernameField);
        fieldsPanel.add(Box.createVerticalStrut(20));
        fieldsPanel.add(passwordField);
        fieldsPanel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setLayout(new GridLayout(2, 1));
        buttonPanel.add(signInButton);
        buttonPanel.add(signUpButton);

        frame.add(welcomePanel, BorderLayout.NORTH);
        frame.add(fieldsPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

    }

}