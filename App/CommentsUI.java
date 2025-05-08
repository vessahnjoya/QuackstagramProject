import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Thgis class provides a user interface for displaying and adding comments under posts,
 * Comments are stored in a text file
 */
public class CommentsUI {
    /**
     * Creates a panel to display and add comments for a given image
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
     * Saves a comment to a txt file
     * @param imageId The ID of the image being commented on
     * @param comment The comment text
     */
    private static void saveComment(String imageId, String comment) {
        // TODO
        // String currentUser = getCurrentUser();
        // String commentEntry = imageId + ": " + currentUser + " says: " + comment + "\n";
        // try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("data", "comments.txt"),
        //         StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
        //     writer.write(commentEntry);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

    /**
     * Loads comments from a txt file and displays them in the text area
     * @param imageId The ID of the image for which comments are loaded
     * @param commentsArea The JTextArea where comments are displayed
     */
    private static void loadComments(String imageId, JTextArea commentsArea) {
        // TODO
        // try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "comments.txt"))) {
        //     String line;
        //     while ((line = reader.readLine()) != null) {
        //         if (line.startsWith(imageId + ": ")) {
        //             String[] parts = line.split(": ", 3);
        //             if (parts.length == 3) {
        //                 String username = parts[1];
        //                 String comment = parts[2];
        //                 commentsArea.append(username + ": " + comment + "\n");
        //             }
        //         }
        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

    /**
     * Getter method for the current logged-in user
     * @return The username of the currently logged-in user
     */
    public static String getCurrentUser() {
        return RefactoredSignIn.getLoggedInUsername();
    }
}