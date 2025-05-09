import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * This class provides interface for home UI and inherits implementation from
 * baseUi class
 */
public class QuakstagramHomeUI extends BaseUI {
    private final int IMAGE_WIDTH = WIDTH - 100; // Width for the image posts
    private final int IMAGE_HEIGHT = 150; // Height for the image posts
    private final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel imageViewPanel;
    private DirectMessagingUI directMessagingUI;

    /**
     * The cnstructor sets, adds default values for frame and initializes UI
     */
    @SuppressWarnings("unused")
    public QuakstagramHomeUI() {
        setTitle("Quakstagram Home");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePanel = new JPanel(new BorderLayout());
        imageViewPanel = new JPanel(new BorderLayout());

        initializeUI();

        cardPanel.add(homePanel, "Home");
        cardPanel.add(imageViewPanel, "ImageView");

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "Home"); // Start with the home view

        // Initialize the messaging UI
        directMessagingUI = new DirectMessagingUI(CommentsUI.getCurrentUser());

        // Add the messaging panel to the card panel
        cardPanel.add(directMessagingUI, "Messages");

        // Header Panel (reuse from InstagramProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height

        // Register Panel
        JLabel lblRegister = new JLabel("Quackstagram 🐥");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        lblRegister.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(lblRegister, BorderLayout.CENTER);

        // Message Icon Button
        ImageIcon msgIcon = new ImageIcon("img/icons/msgIcon.png");
        Image msgIconScale = msgIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        JButton msgIconButton = new JButton(new ImageIcon(msgIconScale));
        msgIconButton.setBorder(BorderFactory.createEmptyBorder());
        msgIconButton.setContentAreaFilled(false);
        msgIconButton.setBorderPainted(false); // Remove border
        msgIconButton.setPreferredSize(new Dimension(30, 30)); // Set preferred size

        // Create a panel to hold the button with padding
        JPanel msgIconPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        msgIconPanel.setBackground(new Color(51, 51, 51)); // same background as header
        msgIconPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10)); // Add padding around the button
        msgIconPanel.add(msgIconButton);
        msgIconButton.addActionListener(e -> {
            directMessagingUI.refreshChat(); // Refresh the chat history
            cardLayout.show(cardPanel, "Messages"); // Switch to the messaging panel
        });

        // Add the message icon button to the header panel
        headerPanel.add(msgIconPanel, BorderLayout.EAST); // Add to the right side of the header

        add(headerPanel, BorderLayout.NORTH);
        // Navigation Bar
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        navigationPanel.add(super.BaseCreateIconButton("img/icons/home.png", "home"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(super.BaseCreateIconButton("img/icons/explore.png", "explore"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(super.BaseCreateIconButton("img/icons/upload.png", "upload"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(super.BaseCreateIconButton("img/icons/notifications.png", "notification"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(super.BaseCreateIconButton("img/icons/profile.png", "profile"));

        add(navigationPanel, BorderLayout.SOUTH);
    }

    /**
     * This method intializes UI components and adds them to the frame
     */
    private void initializeUI() {

        // Content Scroll Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Never allow//
                                                                                                 // horizontal scrolling

        String[][] sampleData = createSampleData();
        populateContentPanel(contentPanel, sampleData);
        add(scrollPane, BorderLayout.CENTER);

        // Set up the home panel
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        homePanel.add(scrollPane, BorderLayout.CENTER);

    }

    /**
     * This method populates the content panel with different posts with formatting
     * and adds them to the frame
     * 
     * @param panel
     * @param sampleData
     */
    private void populateContentPanel(JPanel panel, String[][] sampleData) {

        for (String[] postData : sampleData) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBackground(Color.WHITE); // Set the background color for the item panel
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            itemPanel.setAlignmentX(CENTER_ALIGNMENT);
            JLabel nameLabel = new JLabel(postData[0]);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Crop the image to the fixed size
            JLabel imageLabel = new JLabel();
            imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to image label
            String imageId = new File(postData[3]).getName().split("\\.")[0];
            try {
                BufferedImage originalImage = ImageIO.read(new File(postData[3]));
                BufferedImage croppedImage = originalImage.getSubimage(0, 0,
                        Math.min(originalImage.getWidth(), IMAGE_WIDTH),
                        Math.min(originalImage.getHeight(), IMAGE_HEIGHT));
                ImageIcon imageIcon = new ImageIcon(croppedImage);
                imageLabel.setIcon(imageIcon);
            } catch (IOException ex) {
                // Handle exception: Image file not found or reading error
                imageLabel.setText("Image not found");
            }

            JLabel descriptionLabel = new JLabel(postData[1]);
            descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel likesLabel = new JLabel(postData[2]);
            likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JButton likeButton = new JButton("❤");
            likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            likeButton.setBackground(LIKE_BUTTON_COLOR); // Set the background color for the like button
            likeButton.setOpaque(true);
            likeButton.setBorderPainted(false); // Remove border
            likeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LikeFunctionality.handleLikeAction(imageId, likesLabel);
                }
            });

            // Button for the comment
            ImageIcon commentIcon = new ImageIcon("img/icons/CommentIcon.png");
            Image iconScaled = commentIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            JButton commentIconButton = new JButton(new ImageIcon(iconScaled));
            commentIconButton.setBorder(BorderFactory.createEmptyBorder());
            commentIconButton.setContentAreaFilled(false);
            commentIconButton.setBorderPainted(false); // Remove border

            // Create another Panel to innclude the comment icon on the left
            JPanel commentPanelIcon = new JPanel(new FlowLayout(FlowLayout.LEFT));
            commentPanelIcon.setBackground(Color.WHITE); // Set the background color for the comment panel
            commentPanelIcon.setPreferredSize(new Dimension(WIDTH, 30));
            commentPanelIcon.setMaximumSize(new Dimension(WIDTH, 30));
            commentPanelIcon.add(commentIconButton);

            // Comment Panel where its initially hidden until pressed
            JPanel commentPanel = CommentsUI.createCommentPanel(imageId);
            commentPanel.setVisible(false); // hide the panel at first

            // Action listener for the comment button
            commentIconButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    commentPanel.setVisible(!commentPanel.isVisible()); // show the panel
                    itemPanel.revalidate();
                    itemPanel.repaint();
                }
            });

            // add the item to the item panel
            itemPanel.add(nameLabel);
            itemPanel.add(imageLabel);
            itemPanel.add(descriptionLabel);
            itemPanel.add(likesLabel);
            itemPanel.add(likeButton);
            itemPanel.add(Box.createVerticalGlue());
            itemPanel.add(commentPanelIcon);
            itemPanel.add(commentPanel);

            panel.add(itemPanel);

            // Make the image clickable
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(postData); // Call a method to switch to the image view
                }
            });

            // Grey spacing panel
            JPanel spacingPanel = new JPanel();
            spacingPanel.setPreferredSize(new Dimension(WIDTH - 10, 5)); // Set the height for spacing
            spacingPanel.setBackground(new Color(230, 230, 230)); // Grey color for spacing
            panel.add(spacingPanel);
        }
    }

    /**
     * This method create sample data from users txt file
     * 
     * @return sample data
     */
    private String[][] createSampleData() {
        // TODO
        // Step1: Fecth followed users username
        String currentUser = RefactoredSignIn.getLoggedInUsername();
        String followedUsersQuery = "select u.username from users u join follow f  on u.user_id = f.followed_id" +
                " where f.follower_id = getUser_id(?)";
        StringBuilder followedUsersList = new StringBuilder();

        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(followedUsersQuery)) {
            statement.setString(1, currentUser);
            var result = statement.executeQuery();
            while (result.next()) {
                if (followedUsersList.length() > 0) {
                    followedUsersList.append(",");
                }
                followedUsersList.append(result.getString("username"));

            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch followed Users: " + e.getMessage());
        }

        // Step 2: fetch the followedUsers posts
        String followedUsersPostQuery = "SELECT u.username, p.caption, p.like_count, p.image_path FROM post p" +
                " JOIN users u ON p.user_id = u.user_id" +
                " WHERE u.username IN (SELECT u2.username FROM users u2" +
                " JOIN follow f ON u2.user_id = f.followed_id" +
                " WHERE f.follower_id = getUser_id(?))";

        // Temporary structure to hold the data
        String[][] tempData = new String[100][]; // Assuming a maximum of 100 posts for simplicity
        int count = 0;

        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(followedUsersPostQuery)) {
            statement.setString(1, currentUser);
            var result = statement.executeQuery();
            while (result.next() && count < tempData.length) {
                String imagePoster = result.getString("username");
                String description = result.getString("caption");
                String likes = "Likes: " + result.getInt("like_count");
                String imagePath = result.getString("image_path");

                tempData[count++] = new String[] { imagePoster, description, likes, imagePath };
            }
        } catch (SQLException e) {
            System.out.println("Failed to load followed Users Post: " + e.getMessage());
        }

        // Transfer the data to the final array
        String[][] sampleData = new String[count][];
        System.arraycopy(tempData, 0, sampleData, 0, count);

        return sampleData;
    }

    /**
     * This methods display images with comments and like functionalities
     * 
     * @param postData
     */
    private void displayImage(String[] postData) {
        imageViewPanel.removeAll(); // Clear previous content

        String imageId = new File(postData[3]).getName().split("\\.")[0];
        JLabel likesLabel = new JLabel(postData[2]); // Update this line

        // Display the image
        JLabel fullSizeImageLabel = new JLabel();
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);

        try {
            BufferedImage originalImage = ImageIO.read(new File(postData[3]));
            BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), WIDTH - 20),
                    Math.min(originalImage.getHeight(), HEIGHT - 40));
            ImageIcon imageIcon = new ImageIcon(croppedImage);
            fullSizeImageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            // Handle exception: Image file not found or reading error
            fullSizeImageLabel.setText("Image not found");
        }

        // User Info
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(postData[0]);
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        userPanel.add(userName);// User Name

        JButton likeButton = new JButton("❤");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setBackground(LIKE_BUTTON_COLOR); // Set the background color for the like button
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false); // Remove border
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LikeFunctionality.handleLikeAction(imageId, likesLabel); // Update this line
                refreshDisplayImage(postData, imageId); // Refresh the view
            }
        });
        // Button for the comment text
        ImageIcon commentIcon = new ImageIcon("img/icons/CommentIcon .png");
        Image iconScaled = commentIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton commentIconButton = new JButton(new ImageIcon(iconScaled));
        commentIconButton.setBorder(BorderFactory.createEmptyBorder());
        commentIconButton.setContentAreaFilled(false);
        commentIconButton.setBorderPainted(false); // Remove border
        commentIconButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and display the comment panel
                JPanel commentPanel = CommentsUI.createCommentPanel(imageId);

                // Create a dialog to display the comment panel
                JDialog commentDialog = new JDialog();
                commentDialog.setTitle("Comments");
                commentDialog.setModal(true);
                commentDialog.setSize(300, 400);
                commentDialog.setLocationRelativeTo(null); // Center the dialog
                commentDialog.add(commentPanel);
                commentDialog.setVisible(true);
            }
        });

        // Information panel at the bottom
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(postData[1])); // Description
        infoPanel.add(new JLabel(postData[2])); // Likes
        infoPanel.add(likeButton);

        imageViewPanel.add(fullSizeImageLabel, BorderLayout.CENTER);
        imageViewPanel.add(infoPanel, BorderLayout.SOUTH);
        imageViewPanel.add(userPanel, BorderLayout.NORTH);

        imageViewPanel.revalidate();
        imageViewPanel.repaint();

        cardLayout.show(cardPanel, "ImageView"); // Switch to the image view
    }

    /**
     * This method displays images with updated data
     * 
     * @param postData
     * @param imageId
     */
    // TODO UPDATE
    private void refreshDisplayImage(String[] postData, String imageId) {

        String updateLikesQuery = "SELECT like_count FROM post WHERE image_path = ?";
        String path = "img/uploaded/" + imageId;
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(updateLikesQuery)) {
            statement.setString(1, path);
            var result = statement.executeQuery();
            if (result.next()) {
                int likes = result.getInt("like_count");
                postData[2] = "Likes: " + likes;
            }
        } catch (SQLException e) {
            System.out.println("Failed to refresh Like count for Image_path: " + path + ": " + e.getMessage());
        }
        // Call displayImage with updated postData
        displayImage(postData);
    }
}