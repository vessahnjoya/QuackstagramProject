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

public class CommentsUI {
   public static JPanel createCommentPanel(String imageId) {
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
                    commentsArea.append( getCurrentUser() + " Commented: " + comment + "\n");
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
    private static void saveComment(String imageId, String comment) {
        String currentUser = getCurrentUser();
        String commentEntry = imageId + ": " + currentUser +" say's: "+  comment + "\n";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("data", "comments.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(commentEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methode to load the comment in comment section
    private static void loadComments(String imageId, JTextArea commentsArea) {
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
    public static String getCurrentUser(){
        return RefactoredSignIn.getLoggedInUsername();
    }
}
