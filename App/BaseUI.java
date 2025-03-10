import javax.swing.*;
import java.awt.*;

/**
 * BaseUI is an abstract class that provides common UI components and navigation functionalities
 * and serves a base for other UI components
 */
public abstract class BaseUI extends JFrame {
    public static final int NAV_ICON_SIZE = 20;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 500;
    public static final int IMAGE_SIZE = WIDTH / 3;
    public static final int GRID_IMAGE_SIZE = WIDTH / 3;
    public static final int PROFILE_IMAGE_SIZE = 80;
    private static JLabel pageLabel;

    /**
     * Creates and returns the header panel with a page label
     * @return JPanel representing the header
     */
    public JPanel BaseCreateHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51));
        
        pageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pageLabel.setForeground(Color.WHITE);
        headerPanel.add(pageLabel);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40));
        
        return headerPanel;
    }

    /**
     * Creates and returns the navigation panel with icon buttons for different functionalities
     * @return JPanel representing the navigation bar
     */
    public JPanel BaseCreateNavigationPanel() {
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

    /**
     * Creates an icon button with a specified image and button type
     * @param iconPath   The path to the icon image
     * @param buttonType The type of button (e.g., "home", "profile", "upload")
     * @return JButton representing the navigation button
     */
    @SuppressWarnings("unused")
    public JButton BaseCreateIconButton(String iconPath, String buttonType) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);

        // Define actions based on button type
        switch (buttonType) {
            case "home":
                button.addActionListener(e -> BaseOpenHomeUI());
                break;
            case "profile":
                button.addActionListener(e -> BaseOpenProfileUI());
                break;
            case "notification":
                button.addActionListener(e -> BaseNotificationsUI());
                break;
            case "explore":
                button.addActionListener(e -> BaseExploreUI());
                break;
            case "upload":
                button.addActionListener(e -> BaseImageUploadUI());
                break;
        }
        return button;
    }

    /**
     * Opens the Image Upload UI and disposes of the current frame
     */
    public void BaseImageUploadUI() {
        this.dispose();
        pageLabel = new JLabel("Image Upload 🐥");
        ImageUploadUI upload = new ImageUploadUI();
        upload.setVisible(true);
    }

    /**
     * Opens the Profile UI with the currently logged-in user and disposes of the current frame
     */
    public void BaseOpenProfileUI() {
        this.dispose();

        String loggedInUsername = RefactoredSignIn.getLoggedInUsername();
        if (loggedInUsername == null || loggedInUsername.isEmpty()) {
            System.out.println("Error: No user is logged in!");
            return;
        }

        User user = new User(loggedInUsername);
        InstagramProfileUI profileUI = new InstagramProfileUI(user);
        profileUI.setVisible(true);
    }

    /**
     * Opens the Notifications UI and disposes of the current frame
     */
    public void BaseNotificationsUI() {
        this.dispose();
        pageLabel = new JLabel("Notification 🐥");
        NotificationsUI notificationsUI = new NotificationsUI();
        notificationsUI.setVisible(true);
    }

    /**
     * Opens the Home UI and disposes of the current frame
     */
    public void BaseOpenHomeUI() {
        this.dispose();
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
        homeUI.setVisible(true);
    }

    /**
     * Opens the Explore UI and disposes of the current frame
     */
    public void BaseExploreUI() {
        this.dispose();
        pageLabel = new JLabel("Explore 🐥");
        ExploreUI explore = new ExploreUI();
        explore.setVisible(true);
    }
}
