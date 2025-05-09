import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.awt.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.stream.Stream;

public class InstagramProfileUI extends BaseUI {

    private JPanel contentPanel; // Panel to display the image grid or the clicked image
    private JPanel headerPanel; // Panel for the header
    private JPanel navigationPanel; // Panel for the navigation
    private User currentUser; // User object to store the current user's information

    /**
     * The constructor initializes fields such as current user and counts, and UI
     * 
     * @param user
     */
    public InstagramProfileUI(User user) {
        this.currentUser = user;
        // Initialize counts
        int imageCount = 0;
        int followersCount = 0;
        int followingCount = 0;

        // Step 1: count the number of images posted by the user
        String ImageDetailsQuery = "SELECT COUNT(post_id) AS image_count FROM post WHERE user_id = getUser_id(?)";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(ImageDetailsQuery)) {
            statement.setString(1, user.getUsername());
            var rs = statement.executeQuery();
            if (rs.next()) {
                imageCount = rs.getInt("image_count");
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve number of posts " + e.getMessage());
        }

        // Step 2:calculate followers count
        String followersQuery = "SELECT COUNT(*) as followers FROM follow WHERE followed_id = getUser_id(?)";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(followersQuery)) {
            statement.setString(1, user.getUsername());
            var result = statement.executeQuery();
            if (result.next()) {
                followersCount = result.getInt("followers");
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve followers count " + e.getMessage());
        }

        // Step 3: calculate following count
        String followingQuery = "SELECT COUNT(*) as following FROM follow WHERE follower_id = getUser_id(?)";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(followingQuery)) {
            statement.setString(1, user.getUsername());
            var result = statement.executeQuery();
            if (result.next()) {
                followingCount = result.getInt("following");
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve following count " + e.getMessage());
        }

        String bio = user.getBio();

        // debug statement
        System.out.println("Bio for " + currentUser.getUsername() + ": " + bio);
        currentUser.setBio(bio);

        currentUser.setFollowersCount(followersCount);
        currentUser.setFollowingCount(followingCount);
        currentUser.setPostCount(imageCount);

        // debug statement
        System.out.println("number of posts " + currentUser.getPostsCount());
        System.out.println("following " + currentUser.getFollowingCount());
        System.out.println("followers " + currentUser.getFollowersCount());

        setTitle("DACS Profile");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        headerPanel = createHeaderPanel(); // Initialize header panel
        navigationPanel = super.BaseCreateNavigationPanel(); // Initialize navigation panel

        initializeUI();
    }

    /**
     * This methods initializes UI and adds components to JFrame
     */
    private void initializeUI() {
        getContentPane().removeAll(); // Clear existing components

        // Re-add the header and navigation panels
        add(headerPanel, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.SOUTH);

        // Initialize the image grid
        initializeImageGrid();

        revalidate();
        repaint();
    }

    /**
     * This method creates a header panel containing username info and statistics,
     * profile picture
     * 
     * @return header panel
     */
    @SuppressWarnings("unused")
    private JPanel createHeaderPanel() {
        boolean isCurrentUser = true;
        String loggedInUsername = currentUser.getUsername();

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);

        // Top Part of the Header (Profile Image, Stats, Follow Button)
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        // Profile image
        String profileImageQuery = "Select image_path from post where user_id = getUser_id(?)";
            try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(profileImageQuery)) {
            statement.setString(1, loggedInUsername);
            var rs = statement.executeQuery();
            if(rs.next()){
                ImageIcon profileIcon = new ImageIcon(new ImageIcon(rs.getString("image_path"))
                .getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);
            }
        } catch (SQLException e) {
            System.err.println("Failed to get profile Image for username: " + loggedInUsername + ", Error: " + e.getMessage());
        }
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("img/storage/profile/" + currentUser.getUsername() + ".png")
                .getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);

        // Stats Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        System.out.println("Number of posts for this user" + currentUser.getPostsCount());
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getPostsCount()), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowersCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding

        JButton followButton;
        if (isCurrentUser) {
            followButton = new JButton("Edit Profile");
            followButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    EditProfileUI(currentUser);
                };
            });
        } else {
            followButton = new JButton("Follow");

            //TODO
            // // Check if the current user is already being followed by the logged-in user
            // Path followingFilePath = Paths.get("data", "following.txt");
            // try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
            //     String line;
            //     while ((line = reader.readLine()) != null) {
            //         String[] parts = line.split(":");
            //         if (parts[0].trim().equals(loggedInUsername)) {
            //             String[] followedUsers = parts[1].split(";");
            //             for (String followedUser : followedUsers) {
            //                 if (followedUser.trim().equals(currentUser.getUsername())) {
            //                     followButton.setText("Following");
            //                     break;
            //                 }
            //             }
            //         }
            //     }
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }
            // followButton.addActionListener(e -> {
            //     handleFollowAction(currentUser.getUsername());
            //     followButton.setText("Following");
            // });
        }

        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.setFont(new Font("Arial", Font.BOLD, 12));
        followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height)); // Make the
                                                                                                             // button
                                                                                                             // fill the
                                                                                                             // horizontal
                                                                                                             // space
        followButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
        followButton.setForeground(Color.BLACK);
        followButton.setOpaque(true);
        followButton.setBorderPainted(false);
        followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding

        // Add Stats and Follow Button to a combined Panel
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(followButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        headerPanel.add(topHeaderPanel);

        // Profile Name and Bio Panel
        JPanel profileNameAndBioPanel = new JPanel();
        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

        JLabel profileNameLabel = new JLabel(currentUser.getUsername());
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

        JTextArea profileBio = new JTextArea(currentUser.getBio());
        System.out.println("This is the bio " + currentUser.getUsername());
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides

        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

        headerPanel.add(profileNameAndBioPanel);

        return headerPanel;

    }

    /**
     * This method handles following of users and saves the following
     * 
     * @param usernameToFollow user to follow
     */
    // TODO
    private void handleFollowAction(String usernameToFollow) {
        Path followingFilePath = Paths.get("data", "following.txt");
        Path usersFilePath = Paths.get("data", "users.txt");
        String currentUserUsername = "";

        try {
            // Read the current user's username from users.txt
            try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    currentUserUsername = parts[0];
                }
            }

            System.out.println("Real user is " + currentUserUsername);
            // If currentUserUsername is not empty, process following.txt
            if (!currentUserUsername.isEmpty()) {
                boolean found = false;
                StringBuilder newContent = new StringBuilder();

                // Read and process following.txt
                if (Files.exists(followingFilePath)) {
                    try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(":");
                            if (parts[0].trim().equals(currentUserUsername)) {
                                found = true;
                                if (!line.contains(usernameToFollow)) {
                                    line = line.concat(line.endsWith(":") ? "" : "; ").concat(usernameToFollow);
                                }
                            }
                            newContent.append(line).append("\n");
                        }
                    }
                }

                // If the current user was not found in following.txt, add them
                if (!found) {
                    newContent.append(currentUserUsername).append(": ").append(usernameToFollow).append("\n");
                }

                // Write the updated content back to following.txt
                try (BufferedWriter writer = Files.newBufferedWriter(followingFilePath)) {
                    writer.write(newContent.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates an Image grid that contains users post
     */
    private void initializeImageGrid() {
        contentPanel.removeAll(); // Clear existing content
        contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image grid

        Path imageDir = Paths.get("img", "uploaded");
        try (Stream<Path> paths = Files.list(imageDir)) {
            paths.filter(path -> path.getFileName().toString().startsWith(currentUser.getUsername() + "_"))
                    .forEach(path -> {
                        ImageIcon imageIcon = new ImageIcon(new ImageIcon(path.toString()).getImage()
                                .getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
                        JLabel imageLabel = new JLabel(imageIcon);
                        imageLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                displayImage(imageIcon); // Call method to display the clicked image
                            }
                        });
                        contentPanel.add(imageLabel);
                    });
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle exception (e.g., show a message or log)
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

        revalidate();
        repaint();
    }

    /**
     * This method is used to display users image
     * 
     * @param imageIcon
     */
    @SuppressWarnings("unused")
    private void displayImage(ImageIcon imageIcon) {
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            getContentPane().removeAll(); // Remove all components from the frame
            initializeUI(); // Re-initialize the UI
        });
        contentPanel.add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /**
     * This method creates the statistics panel for user
     * 
     * @param number
     * @param text
     * @return
     */
    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>",
                SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

    /**
     * This method is used to open profile customization
     * 
     * @param user
     */
    private void EditProfileUI(User user) {
        EditProfileUI editProfileUI = new EditProfileUI(user);
        editProfileUI.setVisible(true);
    }

}
