import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

/**
 * This class handles the like functionality
 */
public class LikeFunctionality {
    /**
     * This method handles the like functionality inide the home UI
     * @param imageId
     * @param likesLabel
     */
    //TODO
    static void handleLikeAction(String imageId, JLabel likesLabel) {
        String currentUser = RefactoredSignIn.getLoggedInUsername();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

       // Convert imageId to full path
        String imagePath = "img/uploaded/" + imageId + ".png";


        String updateLikes = "UPDATE post SET like_count = like_count + 1 WHERE image_path = ?";
        String getPostId = "SELECT post_id, user_id FROM post WHERE image_path = ?";
        String notifySQL = "INSERT INTO notification (recipient_id, sender_id, post_id, message, time_stamp) VALUES (?, ?, ?, ?, ?)";

        try (var conn = DatabaseConnection.getConnection()) {
            int postId = -1;
            int ownerId = -1;
            int likerId = getUserIdQuery(currentUser);  // You may need to implement this helper

            // Step 1: Get post_id and post owner
            try (var stmt = conn.prepareStatement(getPostId)) {
                stmt.setString(1, imagePath);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    postId = rs.getInt("post_id");
                    ownerId = rs.getInt("user_id");
                }
            }

            // Step 2: Update like count
            try (var stmt = conn.prepareStatement(updateLikes)) {
                stmt.setString(1, imagePath);
                stmt.executeUpdate();
            }

            // Step 3: Send notification
            try (var stmt = conn.prepareStatement(notifySQL)) {
                stmt.setInt(1, ownerId);
                stmt.setInt(2, likerId);
                stmt.setInt(3, postId);
                stmt.setString(4, currentUser + " liked your post");
                stmt.setString(5, timestamp);
                stmt.executeUpdate();
            }

            // Step 4: Update label
            likesLabel.setText("Liked!");
            JOptionPane.showMessageDialog(null, "You liked the post.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating like in database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
       
    }
     private static int getUserIdQuery(String username) {

            String query = "SELECT user_id FROM users WHERE username = ?";
            try (var conn = DatabaseConnection.getConnection();
                var stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1;
        
        }
}