import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ExploreUI extends BaseUI {

    public ExploreUI() {
        setTitle("Explore");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel headerPanel = super.BaseCreateHeaderPanel();
        JPanel navigationPanel = super.BaseCreateNavigationPanel();
        JPanel mainContentPanel = createMainContentPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel createMainContentPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(" Search Users");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));

        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2)); // 3 columns, auto rows

        String query = """
            SELECT u.username, p.caption, p.image_path, p.like_count
            FROM post p
            JOIN users u ON p.user_id = u.user_id
            ORDER BY RAND()
            LIMIT 20
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String caption = rs.getString("caption");
                String imagePath = rs.getString("image_path");
                int likeCount = rs.getInt("like_count");

                ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage()
                        .getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(imageIcon);

                imageLabel.setToolTipText("<html>@" + username + "<br/>" + caption + "<br/>Likes: " + likeCount + "</html>");

                imageLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        displayImage(username, caption, imagePath, likeCount);
                    }
                });

                imageGridPanel.add(imageLabel);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to load explore posts: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(imageGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(searchPanel);
        mainContentPanel.add(scrollPane);

        return mainContentPanel;
    }

    private void displayImage(String username, String caption, String imagePath, int likes) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Get time posted from DB
        String timeStamp = "";
        String getTimeQuery = "SELECT time_stamp FROM post WHERE image_path = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getTimeQuery)) {
            stmt.setString(1, imagePath);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                timeStamp = rs.getString("time_stamp");
            }
        } catch (SQLException e) {
            timeStamp = "Unknown";
        }

        // Calculate time since posting
        String timeSincePosting = "Unknown";
        if (!timeStamp.isEmpty() && !timeStamp.equals("Unknown")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime postTime = LocalDateTime.parse(timeStamp, formatter);
            LocalDateTime now = LocalDateTime.now();
            long days = ChronoUnit.DAYS.between(postTime, now);
            timeSincePosting = days + " day" + (days != 1 ? "s" : "") + " ago";
        }

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton usernameLabel = new JButton(username);
        JLabel timeLabel = new JLabel(timeSincePosting);
        topPanel.add(usernameLabel, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            ImageIcon imageIcon = new ImageIcon(originalImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            imageLabel.setText("Image not found");
        }

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextArea captionArea = new JTextArea(caption);
        captionArea.setEditable(false);
        int updatedLikes = 0;
        String getLikesQuery = "SELECT like_count FROM post WHERE image_path = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(getLikesQuery)) {
            stmt.setString(1, imagePath);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                updatedLikes = rs.getInt("like_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JLabel likesLabel = new JLabel("Likes: " + updatedLikes);

        bottomPanel.add(captionArea, BorderLayout.CENTER);
        bottomPanel.add(likesLabel, BorderLayout.SOUTH);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            getContentPane().removeAll();
            add(BaseCreateHeaderPanel(), BorderLayout.NORTH);
            add(createMainContentPanel(), BorderLayout.CENTER);
            add(BaseCreateNavigationPanel(), BorderLayout.SOUTH);
            revalidate();
            repaint();
        });

        usernameLabel.addActionListener(e -> {
            User user = new User(username);
            InstagramProfileUI profileUI = new InstagramProfileUI(user);
            profileUI.setVisible(true);
            dispose();
        });

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(imageLabel, BorderLayout.CENTER);
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(BaseCreateHeaderPanel(), BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);
        add(BaseCreateNavigationPanel(), BorderLayout.SOUTH);
        add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
}
