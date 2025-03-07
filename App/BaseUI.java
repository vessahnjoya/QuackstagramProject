
import javax.swing.*;
import java.awt.*;


public abstract class BaseUI extends JFrame {
    public static final int NAV_ICON_SIZE = 20;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 500;
    public static final int IMAGE_SIZE = WIDTH / 3; // Size for each image in the grid
    public static final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images
    public static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private  static  JLabel pageLabel;

    public JPanel BaseCreateHeaderPanel() {

        // Header Panel (reuse from InstagramProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        //  pageLabel = new JLabel(" Explore 🐥");
        pageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pageLabel.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(pageLabel);
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
        navigationPanel.add(BaseCreateIconButton("img/icons/explore.png", "explore"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(BaseCreateIconButton("img/icons/upload.png", "upload"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(BaseCreateIconButton("img/icons/notifications.png", "notification"));
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
        } else if ("upload".equals(buttonType)) {
            button.addActionListener(e -> BaseImageUploadUI());
        }
        return button;

    }

    public void BaseImageUploadUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        pageLabel = new JLabel("Image Upload 🐥");
        ImageUploadUI upload = new ImageUploadUI();
        upload.setVisible(true);
    }

    public void BaseOpenProfileUI() {
        this.dispose();

        // Access the logged-in username correctly
        String loggedInUsername = RefactoredSignIn.getLoggedInUsername(); // Fix: Use the getter method

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
        pageLabel = new JLabel("Notification 🐥");
        NotificationsUI notificationsUI = new NotificationsUI();
        notificationsUI.setVisible(true);
    }

    public void BaseOpenHomeUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        // pageLabel = new JLabel("Quackstagram🐥");
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
        homeUI.setVisible(true);
    }

    public void BaseExploreUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        pageLabel = new JLabel("Explore 🐥");
        ExploreUI explore = new ExploreUI();
        explore.setVisible(true);
    }
}
