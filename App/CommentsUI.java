import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class provides a user interface for displaying and adding comments
 * under posts,
 * Comments are stored in a text file
 */
public class CommentsUI {
    /**
     * Creates a panel to display and add comments for a given image
     * 
     * @param imageId The ID of the image for which comments are displayed
     * @return JPanel representing the comment section
     */
    public static JPanel createCommentPanel(String imageId) {
        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BorderLayout());
        commentPanel.setBackground(Color.WHITE);
        commentPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Text area to display comments
        JTextArea commentsArea = new JTextArea();
        commentsArea.setEditable(false);
        commentsArea.setBackground(new Color(240, 240, 240));
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);

        // Scroll pane for comments
        JScrollPane scrollPane = new JScrollPane(commentsArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        commentPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel to add new comments
        JPanel newCommentPanel = new JPanel(new BorderLayout());
        JTextField commentField = new JTextField();
        JButton submitButton = new JButton("➡︎");

        // Load existing comments
        loadComments(imageId, commentsArea);

        // Action listener for submitting comments
        ActionListener submitAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comment = commentField.getText().trim();
                if (!comment.isEmpty()) {
                    saveComment(imageId, comment);
                    commentsArea.append(getCurrentUser() + " Commented: " + comment + "\n");
                    commentField.setText("");
                }
            }
        };
        submitButton.addActionListener(submitAction);

        // Allow submitting comment by pressing Enter.
        commentField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitAction.actionPerformed(null);
                }
            }
        });

        newCommentPanel.add(commentField, BorderLayout.CENTER);
        newCommentPanel.add(submitButton, BorderLayout.EAST);
        commentPanel.add(newCommentPanel, BorderLayout.SOUTH);

        return commentPanel;
    }

    /**
     * Saves a comment to database
     * 
     * @param imageId The ID of the image being commented on
     * @param comment The comment text
     */
    private static void saveComment(String imageId, String comment) {
        String imgId = imageId;
        int postId = findPostIdQuery(imgId);
        String currentUser = getCurrentUser();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String saveCommentQuery = "INSERT into comment (time_stamp, user_id, comment_text) values (?, getUser_id(?), ?) WHERE post_id = ?";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(saveCommentQuery)) {
            statement.setString(1, timestamp);
            statement.setString(2, currentUser);
            statement.setString(3, comment);
            statement.setInt(4, postId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to save Comment " + e.getMessage());
        }
    }

    /**
     * Loads comments from database and displays them in the text area
     * 
     * @param imageId      The ID of the image for which comments are loaded
     * @param commentsArea The JTextArea where comments are displayed
     */
    private static void loadComments(String imageId, JTextArea commentsArea) {
        String imgId = imageId;
        int postId = findPostIdQuery(imgId);

        String loadCommentQuery = "SELECT get_Username(user_id) as username, comment_text FROM comment WHERE post_id = ? ";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(loadCommentQuery)) {
            statement.setInt(1, postId);
            var result = statement.executeQuery();
            while (result.next()) {
                String username = result.getString("username");
                String comment = result.getString("comment_text");
                commentsArea.append(username + ": " + comment + "\n");
            }
        } catch (SQLException e) {
            System.out.println("Failed to load Comments: " + e.getMessage());
        }
    }

    /**
     * Getter method for the current logged-in user
     * 
     * @return The username of the currently logged-in user
     */
    public static String getCurrentUser() {
        return RefactoredSignIn.getLoggedInUsername();
    }

    /**
     * This method is used to find the post_id given an image path
     * NOT FUNCTIONAL !!!!!
     * 
     * @param imageId
     * @return post_id
     */
    private static int findPostIdQuery(String imageId) {
        String findPostIdQuery = "SELECT post_id FROM post WHERE image_path = ?";
        int postId = 0;
         String path = "img/uploaded/" +"_" + imageId +".png";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(findPostIdQuery)) {
            statement.setString(1, path);
            var result = statement.executeQuery();
            if (result.next()) {
                postId = result.getInt("post_id");
                return postId;
            }
        } catch (SQLException e) {
            System.out.println("Failed to find post_id: " + e.getMessage());
        }
        return postId;
    }
}
