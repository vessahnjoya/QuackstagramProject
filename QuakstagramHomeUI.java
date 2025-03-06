

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class QuakstagramHomeUI extends BaseUI {
    private  final int IMAGE_WIDTH = WIDTH - 100; // Width for the image posts
    private  final int IMAGE_HEIGHT = 150; // Height for the image posts
    private  final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel imageViewPanel;
    private DirectMessagingUI directMessagingUI;

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
        directMessagingUI = new DirectMessagingUI(getCurrentUser());

        // Add the messaging panel to the card panel
        cardPanel.add(directMessagingUI, "Messages");

        // Header Panel (reuse from InstagramProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel = new JPanel(new BorderLayout());
          headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
          headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
          
          //Register Panel
          JLabel lblRegister = new JLabel("🐥 Quackstagram 🐥");
          lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
          lblRegister.setForeground(Color.WHITE); // Set the text color to white
          lblRegister.setHorizontalAlignment(SwingConstants.CENTER);

          headerPanel.add(lblRegister, BorderLayout.CENTER);
        
          // Message Icon Button 
         ImageIcon msgIcon = new ImageIcon("img/icons/msgIcon.png");
         Image msgIconScale = msgIcon.getImage().getScaledInstance(35,35, Image.SCALE_SMOOTH);
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
        navigationPanel.add(super.BaseCreateIconButton("img/icons/search.png", "explore"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(super.BaseCreateIconButton("img/icons/add.png", "add"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(super.BaseCreateIconButton("img/icons/heart.png", "notification"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(super.BaseCreateIconButton("img/icons/profile.png", "profile"));

        add(navigationPanel, BorderLayout.SOUTH);
    }

    private void initializeUI() {

        // Content Scroll Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Never allow// horizontal scrolling

        String[][] sampleData = createSampleData();
        populateContentPanel(contentPanel, sampleData);
        add(scrollPane, BorderLayout.CENTER);

        // Set up the home panel
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        homePanel.add(scrollPane, BorderLayout.CENTER);

    }

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
                    handleLikeAction(imageId, likesLabel);
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
            JPanel commentPanel = createCommentPanel(imageId);
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

    private void handleLikeAction(String imageId, JLabel likesLabel) {
        Path detailsPath = Paths.get("img", "image_details.txt");
        Path likesTrackingPath = Paths.get("data", "likes_tracking.txt");
        StringBuilder newContent = new StringBuilder();
        boolean updated = false;
        boolean alreadyLiked = false;
        String currentUser = RefactoredSignIn.getLoggedInUsername();
        String imageOwner = "";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // Ensure likes_tracking.txt exists
        try {
            if (!Files.exists(likesTrackingPath)) {
                Files.createFile(likesTrackingPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Retrieve the current user from users.txt
        try (BufferedReader userReader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = userReader.readLine();
            if (line != null) {
                currentUser = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Check if the user has already liked this post
        try (BufferedReader likesReader = Files.newBufferedReader(likesTrackingPath)) {
            String line;
            while ((line = likesReader.readLine()) != null) {
                if (line.equals(currentUser + ";" + imageId)) {
                    alreadyLiked = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (alreadyLiked) {
            JOptionPane.showMessageDialog(null, "You have already liked this post!", "Like Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // Read and update image_details.txt
        try (BufferedReader reader = Files.newBufferedReader(detailsPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ImageID: " + imageId)) {
                    String[] parts = line.split(", ");
                    imageOwner = parts[1].split(": ")[1];
                    int likes = Integer.parseInt(parts[4].split(": ")[1]);
                    likes++; // Increment the likes count
                    parts[4] = "Likes: " + likes;
                    line = String.join(", ", parts);

                    // Update the UI
                    likesLabel.setText("Likes: " + likes);
                    updated = true;
                }
                newContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write updated likes back to image_details.txt
        if (updated) {
            try (BufferedWriter writer = Files.newBufferedWriter(detailsPath)) {
                writer.write(newContent.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Record the like in notifications.txt
            String notification = String.format("%s; %s; %s; %s\n", imageOwner, currentUser, imageId, timestamp);
            try (BufferedWriter notificationWriter = Files.newBufferedWriter(Paths.get("data", "notifications.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                notificationWriter.write(notification);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Record the like in likes_tracking.txt to prevent duplicate likes
            try (BufferedWriter likesWriter = Files.newBufferedWriter(likesTrackingPath, StandardOpenOption.APPEND)) {
                likesWriter.write(currentUser + ";" + imageId);
                likesWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String[][] createSampleData() {
        String currentUser = RefactoredSignIn.getLoggedInUsername();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {  // Iterate through each line
                String[] parts = line.split(":");

                if (parts.length > 0 && parts[0].trim().equalsIgnoreCase(currentUser)) {
                    currentUser = parts[0].trim();
                    break; // Stop searching once found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String followedUsers = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "following.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(currentUser + ":")) {
                    followedUsers = line.split(":")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (followedUsers.isEmpty()) {
            return new String[0][];
        }

        // Temporary structure to hold the data
        String[][] tempData = new String[100][]; // Assuming a maximum of 100 posts for simplicity
        int count = 0;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("img", "image_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null && count < tempData.length) {
                String[] details = line.split(", ");
                String imagePoster ="";
                if (details.length > 1 && details[1].contains(": ")) {
                    String[] posterSplit = details[1].split(": ");
                    if (posterSplit.length > 1) {
                        imagePoster = posterSplit[1]; // Safe to access
                    }
                }
                if (followedUsers.contains(imagePoster)) {
                    String imagePath = ""; // Assuming PNG format
                    if (details.length > 0 && details[0].contains(": ")) {
                        String[] imageSplit = details[0].split(": ");
                        imagePath = (imageSplit.length > 1) ? "img/uploaded/" + imageSplit[1] + ".png" : "img/default.png";
                    }

                    String description = "";
                    if (details.length > 2 && details[2].contains(": ")) {
                        String[] descSplit = details[2].split(": ");
                        description = (descSplit.length > 1) ? descSplit[1] : "No description";
                    }

                    String likes = "";
                    if (details.length > 4 && details[4].contains(": ")) {
                        String[] likesSplit = details[4].split(": ");
                        likes = (likesSplit.length > 1) ? "Likes: " + likesSplit[1] : "Likes: 0";
                    }

                    tempData[count++] = new String[]{imagePoster, description, likes, imagePath};
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Transfer the data to the final array
        String[][] sampleData = new String[count][];
        System.arraycopy(tempData, 0, sampleData, 0, count);

        return sampleData;
    }

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
                handleLikeAction(imageId, likesLabel); // Update this line
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
                JPanel commentPanel = createCommentPanel(imageId);
        
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

    private void refreshDisplayImage(String[] postData, String imageId) {
        // Read updated likes count from image_details.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("img", "image_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ImageID: " + imageId)) {
                    String likes = line.split(", ")[4].split(": ")[1];
                    postData[2] = "Likes: " + likes;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call displayImage with updated postData
        displayImage(postData);
    }
    private JPanel createCommentPanel(String imageId) {
        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BorderLayout());
        commentPanel.setBackground(Color.WHITE);
        commentPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); //  a border for better visualization 
    
        // Text area to display comments
        JTextArea commentsArea = new JTextArea();
        commentsArea.setEditable(false); // User will not be able to change the comments 
        commentsArea.setBackground(new Color(240, 240, 240)); // Light gray background
        commentsArea.setLineWrap(true); // this will enable the text area panel to be the same as the comment panel 
        commentsArea.setWrapStyleWord(true); 

        // The scroll bar for the comment 
        JScrollPane scrollPane = new JScrollPane(commentsArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove the border of the scroll pane
        commentPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel to add new comments
        JPanel newCommentPanel = new JPanel(new BorderLayout());
        JTextField commentField = new JTextField();
        JButton submitButton = new JButton("➡︎");
    
        // Load existing comments
        loadComments(imageId, commentsArea);
    
        // Add action listener to the submit button
        ActionListener submitAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comment = commentField.getText().trim();
                if (!comment.isEmpty()) {
                    saveComment(imageId, comment);
                    commentsArea.append( getCurrentUser() + comment + "\n");
                    commentField.setText("");
                }
            }
        };
        submitButton.addActionListener(submitAction);
        
        // Add the fact that wehn you press enter it also sends the comment 
        commentField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                submitAction.actionPerformed(null); // makes the same action as submit 
              }
          }
     });
    
        newCommentPanel.add(commentField, BorderLayout.CENTER);
        newCommentPanel.add(submitButton, BorderLayout.EAST);
        commentPanel.add(newCommentPanel, BorderLayout.SOUTH);

        return commentPanel;
    }

    // methode to save the comment in local repo
    private void saveComment(String imageId, String comment) {
        String currentUser = getCurrentUser();
        String commentEntry = imageId + ": " + currentUser + comment + "\n";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("data", "comments.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(commentEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methode to load the comment in comment section 
    private void loadComments(String imageId, JTextArea commentsArea) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "comments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(imageId + ": ")) {
                    String[] parts = line.split(": ", 3); 
                    if (parts.length == 3) {
                        String username = parts[1];
                        String comment = parts[2];
                        commentsArea.append(username + ": " + comment + "\n"); // Displays the username and comment
                    }
            }
        }
     } catch (IOException e) {
        }
    }

    // Method to get the name of user 
    // private String getCurrentUser() {
    //     String currentUser = "";
    //     try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
    //         String line = reader.readLine();
    //         if (line != null) {
    //             currentUser = line.split(":")[0].trim(); 
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return currentUser + " say's: ";
    // }

    private String getCurrentUser(){
        return RefactoredSignIn.getLoggedInUsername();
    }
}
