import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class provides user interface for communication with other users
 */
public class DirectMessagingUI extends JPanel {
    private String currentUser;
    private String selectedUser;
    private JTextArea chatArea;
    private JLabel usernameLabel;

    /**
     * The constructor initializes the currentUser, and UI
     * 
     * @param currentUser this is the logged In user
     */
    public DirectMessagingUI(String currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        initializeUI();
    }

    /**
     * This method initializes the UI by creating a panel with users and textArea
     */
    @SuppressWarnings("unused")
    private void initializeUI() {
        // Panel for the list of users
        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(Color.WHITE);
        userListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String userListQuery = "SELECT username from users WHERE username != ?";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(userListQuery)) {
            statement.setString(1, currentUser);
            var result = statement.executeQuery();
            while (result.next()) {
                String username = result.getString("username");
                JButton userButton = new JButton(username);
                userButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                userButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                userButton.addActionListener(e -> openChat(username));
                userListPanel.add(userButton);
            }

        } catch (SQLException e) {
            System.out.println("Failed to Load users " + e.getMessage());
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
                saveMessage(currentUser, selectedUser, message);
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
            getParent().getParent().setVisible(false);
            QuakstagramHomeUI home = new QuakstagramHomeUI();
            home.setVisible(true);
            ;
        });
        add(backButton, BorderLayout.NORTH);
    }

    /**
     * This method opens the chat upon clicking of a username
     * 
     * @param username this is the selectedUser
     */
    private void openChat(String username) {
        selectedUser = username;
        usernameLabel.setText("Texting " + username); // Set the username label
        chatArea.setText(""); // Clear the chat area
        loadChatHistory(selectedUser);
    }

    /**
     * This method is used to load previous messages between two users
     * 
     * @param username this is the receiver
     */
    private void loadChatHistory(String username) {
        // TODO
        String loadChatHistoryQuery = "SELECT sender_id, receiver_id, message_text FROM message WHERE  (sender_id = getUser_id(?) AND receiver_id = getUser_id(?) )"
                +
                "UNION ALL SELECT sender_id, receiver_id, message_text FROM message WHERE (sender_id = getUser_id(?) and receiver_id = getUser_id(?) )";

        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(loadChatHistoryQuery)) {
            statement.setString(1, username);
            statement.setString(2, currentUser);
            statement.setString(3, currentUser);
            statement.setString(4, username);
            var result = statement.executeQuery();
            while (result.next()) {
                int senderId = result.getInt("sender_id");
                String message = result.getString("message_text");
                String senderUsername = getUsernameQuery(senderId);
                chatArea.append(senderUsername + ": " + message + "\n");

            }

        } catch (SQLException e) {
            System.out.println(
                    "Failed to Load chat History between " + username + " and " + currentUser + " : " + e.getMessage());
        }
    }

    /**
     * Helper method to retreieve a username from database
     * 
     * @param senderId
     * @return
     */
    private String getUsernameQuery(int Id) {
        String username = "";
        String query = "SELECT get_Username(?) as username";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(query)) {
            statement.setInt(1, Id);
            var result = statement.executeQuery();
            if (result.next()) {
                username = result.getString("username");
                return username;
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve Username: " + e.getMessage());
        }
        return null;
    }

    /**
     * Helper method to retreieve a username from database
     * 
     * @param senderId
     * @return
     */
    private int getUserIdQuery(String username) {
        int user_id;
        String query = "SELECT getUser_id(?) as user_id";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            var result = statement.executeQuery();
            if (result.next()) {
                user_id = result.getInt("user_id");
                return user_id;
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve Username: " + e.getMessage());
        }
        return -1;
    }

    /**
     * This method is used to save Messages
     * 
     * @param sender   logged in user sending message
     * @param receiver selected user
     * @param message
     */
    private void saveMessage(String sender, String receiver, String message) {
        int sender_Id = getUserIdQuery(sender);
        int receiver_Id = getUserIdQuery(receiver);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String saveMessageQuery = "INSERT INTO message (sender_id, receiver_id, message_text, time_stamp) VALUES (?, ?, ?, ?)";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(saveMessageQuery)) {
            statement.setInt(1, sender_Id);
            statement.setInt(2, receiver_Id);
            statement.setString(3, message);
            statement.setString(4, timestamp);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to save message: " + e.getMessage());
        }
    }

    /**
     * This method ensures that the chat doesn't refresh itself again when a chat is
     * already opened upon misclick
     */
    public void refreshChat() {
        if (selectedUser != null) {
            openChat(selectedUser); // Reload the chat history for the selected user
        }
    }
}
