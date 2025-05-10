import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.*;

public class NotificationsUI extends BaseUI {

    public NotificationsUI() {
        setTitle("Notifications");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        JPanel headerPanel = super.BaseCreateHeaderPanel();
        JPanel navigationPanel = super.BaseCreateNavigationPanel();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        String currentUsername = RefactoredSignIn.getLoggedInUsername();

        String query = """
            SELECT get_Username(sender_id) AS sender_name, message, time_stamp
            FROM notification
            WHERE recipient_id = getUser_id(?)
            ORDER BY time_stamp DESC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, currentUsername);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String sender = rs.getString("sender_name");
                String message = rs.getString("message");
                String timestamp = rs.getString("time_stamp");

                String notificationMessage = sender + " ➤ " + message + " - " + getElapsedTime(timestamp) + " ago";

                JPanel notificationPanel = new JPanel(new BorderLayout());
                notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                JLabel notificationLabel = new JLabel(notificationMessage);
                notificationPanel.add(notificationLabel, BorderLayout.CENTER);

                contentPanel.add(notificationPanel);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to load notifications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private String getElapsedTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeOfNotification = LocalDateTime.parse(timestamp, formatter);
        LocalDateTime currentTime = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
        long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

        StringBuilder timeElapsed = new StringBuilder();
        if (daysBetween > 0) {
            timeElapsed.append(daysBetween).append(" day").append(daysBetween > 1 ? "s" : "");
        }
        if (minutesBetween > 0) {
            if (daysBetween > 0) {
                timeElapsed.append(" and ");
            }
            timeElapsed.append(minutesBetween).append(" minute").append(minutesBetween > 1 ? "s" : "");
        }
        return timeElapsed.toString();
    }
}
