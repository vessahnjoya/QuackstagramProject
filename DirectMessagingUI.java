

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class DirectMessagingUI extends JPanel {
    private String currentUser;
    private String selectedUser;
    private JTextArea chatArea;
    private JLabel usernameLabel; // Label to display the selected username

    public DirectMessagingUI(String currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        initializeUI();
    }

    @SuppressWarnings("unused")
    private void initializeUI() {
        // Panel for the list of users
        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(Color.WHITE);
        userListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // List of the user except the user's sending the message
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String username = line.split(":")[0].trim();
                if (!username.equals(currentUser)) {
                    JButton userButton = new JButton(username);
                    userButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                    userButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    userButton.addActionListener(e -> openChat(username));
                    userListPanel.add(userButton);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add the user to the chat panel
        JScrollPane userListScrollPane = new JScrollPane(userListPanel);
        add(userListScrollPane, BorderLayout.WEST);

        // Panel for the chat area
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(Color.WHITE);
        chatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Label to know you are texting 
        usernameLabel = new JLabel();
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set font and size
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        chatPanel.add(usernameLabel, BorderLayout.NORTH); // Add the label to the top of the chat panel


        // Display the messages in the chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(240, 240, 240));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        // Input field and send button
        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("➡︎");
        sendButton.addActionListener(e -> {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                sendMessage(currentUser, selectedUser, message);
                chatArea.append(currentUser + message + "\n");
                inputField.setText("");
            }
        });

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        // Add the chat panel to the message panel
        add(chatPanel, BorderLayout.CENTER);

        // Button to go back to home page 
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) getParent().getParent().getLayout();
            cardLayout.show(getParent().getParent(), "Home");
        });
        add(backButton, BorderLayout.NORTH);
    }
    // Open the chat with User
    private void openChat(String username) {
        selectedUser = username;
        usernameLabel.setText("Texting " + username); // Set the username label
        chatArea.setText(""); // Clear the chat area
        loadChatHistory(selectedUser);
    }
    // Method to get the old messages 
    private void loadChatHistory(String username) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "messages.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ", 3);
                if (parts.length == 3) {
                    String sender = parts[0];
                    String receiver = parts[1];
                    String message = parts[2];
                    if ((sender.equals(currentUser) && receiver.equals(username)) ||
                        (sender.equals(username) && receiver.equals(currentUser))) {
                        chatArea.append(sender + ": " + message + "\n");
                    }
                }else {
                    System.out.println("Invalid message format: " + line); // Debug statement
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method to send messages
    private void sendMessage(String sender, String receiver, String message) {
        String messageEntry = sender + ": " + receiver + ": " + message + "\n";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("data", "messages.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(messageEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // This method ensures that the chat doesn't refresh itself again when click on the user 
    public void refreshChat() {
        if (selectedUser != null) {
            openChat(selectedUser); // Reload the chat history for the selected user
        }
    }
}


