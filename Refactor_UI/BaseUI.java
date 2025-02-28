package Refactor_UI;

import User.*;
import javax.swing.*;
import java.awt.*;
import New_Refactor_Sign_In.*;


public class BaseUI extends JFrame {
    private static final int NAV_ICON_SIZE = 20;

    public JPanel BaseCreateHeaderPanel() {

        // Header Panel (reuse from InstagramProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel(" Explore 🐥");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;
    }

    public JPanel BaseCreateNavigationPanel() {
        // Create and return the navigation panel
        // Navigation Bar
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        navigationPanel.add(BaseCreateIconButton("img/icons/home.png", "home"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(BaseCreateIconButton("img/icons/search.png", "explore"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(BaseCreateIconButton("img/icons/add.png", "add"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(BaseCreateIconButton("img/icons/heart.png", "notification"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(BaseCreateIconButton("img/icons/profile.png", "profile"));

        return navigationPanel;
    }

    @SuppressWarnings("unused")
    public JButton BaseCreateIconButton(String iconPath, String buttonType) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);

        // Define actions based on button type
        if ("home".equals(buttonType)) {
            button.addActionListener(e -> BaseOpenHomeUI());
        } else if ("profile".equals(buttonType)) {
            button.addActionListener(e -> BaseOpenProfileUI());
        } else if ("notification".equals(buttonType)) {
            button.addActionListener(e -> BaseNotificationsUI());
        } else if ("explore".equals(buttonType)) {
            button.addActionListener(e -> BaseExploreUI());
        } else if ("add".equals(buttonType)) {
            button.addActionListener(e -> BaseImageUploadUI());
        }
        return button;

    }

    public void BaseImageUploadUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        ImageUploadUI upload = new ImageUploadUI();
        upload.setVisible(true);
    }

    public void BaseOpenProfileUI() {
        this.dispose();

        // Access the logged-in username correctly
        String loggedInUsername = RefactoredSignIn.getLoggedInUsername(); // ✅ Fix: Use the getter method

        if (loggedInUsername == null || loggedInUsername.isEmpty()) {
            System.out.println("Error: No user is logged in!");
            return;
        }

        // Open profile with the correct user
        User user = new User(loggedInUsername);
        InstagramProfileUI profileUI = new InstagramProfileUI(user);
        profileUI.setVisible(true);
    }


    public void BaseNotificationsUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        NotificationsUI notificationsUI = new NotificationsUI();
        notificationsUI.setVisible(true);
    }

    public void BaseOpenHomeUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
        homeUI.setVisible(true);
    }

    public void BaseExploreUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        ExploreUI explore = new ExploreUI();
        explore.setVisible(true);
    }
}
