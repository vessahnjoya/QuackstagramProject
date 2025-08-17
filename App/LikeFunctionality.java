import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

/**
 * This class handles the like functionality
 */
public class LikeFunctionality {
    /**
     * This method handles the like functionality inside the home UI
     * @param imageId
     * @param likesLabel
     */
    static void handleLikeAction(String imageId, JLabel likesLabel) {
        String currentUser = RefactoredSignIn.getLoggedInUsername();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String imagePath = "img/uploaded/" + imageId + ".png";

        String getPostId = "SELECT post_id, user_id FROM post WHERE image_path = ?";
        String checkLike = "SELECT * FROM like_table WHERE user_id = ? AND post_id = ?";
        String insertLike = "INSERT INTO like_table (user_id, post_id, time_stamp) VALUES (?, ?, ?)";
        String updateLikes = "UPDATE post SET like_count = like_count + 1 WHERE image_path = ?";
        String notifySQL = "INSERT INTO notification (recipient_id, sender_id, post_id, message, time_stamp) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            int postId = -1;
            int ownerId = -1;
            int likerId = getUserIdQuery(currentUser);

            // Step 1: Get post ID and owner ID
            try (PreparedStatement stmt = conn.prepareStatement(getPostId)) {
                stmt.setString(1, imagePath);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    postId = rs.getInt("post_id");
                    ownerId = rs.getInt("user_id");
                }
            }

            // Step 2: Check if the user already liked the post
            try (PreparedStatement stmt = conn.prepareStatement(checkLike)) {
                stmt.setInt(1, likerId);
                stmt.setInt(2, postId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "You already liked this post.");
                    return;
                }
            }

            // Step 3: Insert into like_table
            try (PreparedStatement stmt = conn.prepareStatement(insertLike)) {
                stmt.setInt(1, likerId);
                stmt.setInt(2, postId);
                stmt.setString(3, timestamp);
                stmt.executeUpdate();
            }

            // Step 4: Update like count in post table
            try (PreparedStatement stmt = conn.prepareStatement(updateLikes)) {
                stmt.setString(1, imagePath);
                stmt.executeUpdate();
            }

            // Step 5: Add notification
            try (PreparedStatement stmt = conn.prepareStatement(notifySQL)) {
                stmt.setInt(1, ownerId);
                stmt.setInt(2, likerId);
                stmt.setInt(3, postId);
                stmt.setString(4, currentUser + " liked your post");
                stmt.setString(5, timestamp);
                stmt.executeUpdate();
            }

            // Step 6: Update label
            likesLabel.setText("Liked!");
            JOptionPane.showMessageDialog(null, "You liked the post.");
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating like in database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static int getUserIdQuery(String username) {
        String query = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
                        conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
