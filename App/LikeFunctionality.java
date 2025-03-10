import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * This class handles the like functionality
 */
public class LikeFunctionality {
    /**
     * This method handles the like functionality inide the home UI
     * @param imageId
     * @param likesLabel
     */
    static void handleLikeAction(String imageId, JLabel likesLabel) {
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
            JOptionPane.showMessageDialog(null, "You have already liked this post!", "Like Failed",
                    JOptionPane.ERROR_MESSAGE);
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
            try (BufferedWriter notificationWriter = Files.newBufferedWriter(Paths.get("data", "notifications.txt"),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
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
}
