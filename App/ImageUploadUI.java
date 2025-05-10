import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class provides an interface for uploading of an image with a caption
 */
public class ImageUploadUI extends BaseUI {

    private JLabel imagePreviewLabel;
    private JTextArea bioTextArea;
    private JButton uploadButton;
    private JButton saveButton;

    public ImageUploadUI() {
        setTitle("Upload Image");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    /**
     * This method initializes the ui components and add them to the frame
     */
    private void initializeUI() {
        JPanel headerPanel = super.BaseCreateHeaderPanel(); // Reuse the createHeaderPanel method
        JPanel navigationPanel = super.BaseCreateNavigationPanel(); // Reuse the createNavigationPanel method

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Image preview
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));

        // Set an initial empty icon to the imagePreviewLabel
        ImageIcon emptyImageIcon = new ImageIcon();
        imagePreviewLabel.setIcon(emptyImageIcon);

        contentPanel.add(imagePreviewLabel);

        // Bio text area
        bioTextArea = new JTextArea("Enter a caption");
        bioTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioTextArea.setLineWrap(true);
        bioTextArea.setWrapStyleWord(true);
        JScrollPane bioScrollPane = new JScrollPane(bioTextArea);
        bioScrollPane.setPreferredSize(new Dimension(WIDTH - 50, HEIGHT / 6));
        contentPanel.add(bioScrollPane);

        // Upload button
        uploadButton = new JButton("Upload Image");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(this::uploadAction);
        contentPanel.add(uploadButton);

        // Save button (for bio)
        saveButton = new JButton("Save Caption");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(this::saveBioAction);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    /**
     * This method handles upload action precisely file selction and displays an
     * error if the file format is not supported
     * 
     * @param event defines if the action has occured
     */
    private void uploadAction(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an image file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String username = RefactoredSignIn.getLoggedInUsername(); // Read username from users.txt
                int imageId = getNextImageId(username);
                String fileExtension = getFileExtension(selectedFile);
                String newFileName = username + "_" + imageId + "." + fileExtension;

                Path destPath = Paths.get("img", "uploaded", newFileName);
                Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

                // Save the bio and image ID to a text file
                saveImageInfo(username + "_" + imageId, username, bioTextArea.getText());

                // Load the image from the saved path
                ImageIcon imageIcon = new ImageIcon(destPath.toString());

                // Check if imagePreviewLabel has a valid size
                if (imagePreviewLabel.getWidth() > 0 && imagePreviewLabel.getHeight() > 0) {
                    Image image = imageIcon.getImage();

                    // Calculate the dimensions for the image preview
                    int previewWidth = imagePreviewLabel.getWidth();
                    int previewHeight = imagePreviewLabel.getHeight();
                    int imageWidth = image.getWidth(null);
                    int imageHeight = image.getHeight(null);
                    double widthRatio = (double) previewWidth / imageWidth;
                    double heightRatio = (double) previewHeight / imageHeight;
                    double scale = Math.min(widthRatio, heightRatio);
                    int scaledWidth = (int) (scale * imageWidth);
                    int scaledHeight = (int) (scale * imageHeight);

                    // Set the image icon with the scaled image
                    imageIcon.setImage(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
                }

                imagePreviewLabel.setIcon(imageIcon);

                // // Update the flag to indicate that an image has been uploaded
                // imageUploaded = true;

                // Change the text of the upload button
                uploadButton.setText("Upload Another Image");

                JOptionPane.showMessageDialog(this, "Image uploaded and preview updated!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * This method ensures the pictures are saved in the correct directory and
     * 
     * @param username user uploading image
     * @return Id + 1
     * @throws IOException
     */
    private int getNextImageId(String username) throws IOException {
        Path storageDir = Paths.get("img", "uploaded"); // Ensure this is the directory where images are saved
        if (!Files.exists(storageDir)) {
            Files.createDirectories(storageDir);
        }

        int maxId = 0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(storageDir, username + "_*")) {
            for (Path path : stream) {
                String fileName = path.getFileName().toString();
                int idEndIndex = fileName.lastIndexOf('.');
                if (idEndIndex != -1) {
                    String idStr = fileName.substring(username.length() + 1, idEndIndex);
                    try {
                        int id = Integer.parseInt(idStr);
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (NumberFormatException ex) {
                        // Ignore filenames that do not have a valid numeric ID
                    }
                }
            }
        }
        return maxId + 1; // Return the next available ID
    }

    /**
     * This method saves the user post into post table
     * 
     * @param imageId
     * @param username
     * @param bio
     */
    private void saveImageInfo(String imageId, String username, String caption) {
        String path = "img/uploaded/"+ imageId +".png";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String saveImage = "INSERT INTO post(user_id, caption, image_path, time_stamp, like_count) VALUES (getUser_id(?), ?, ?, ?, ?)";
        try (var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(saveImage)) {
            statement.setString(1, username);
            statement.setString(2, caption);
            statement.setString(3, path);
            statement.setString(4, timestamp);
            statement.setInt(5, 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to save user post: " + e.getMessage());
        }
    }
/**
 * This method is used to get the filextension
 * @param file
 * @return
 */
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1);
    }

    /**
     * This method save the bio
     * 
     * @param event
     */
    private void saveBioAction(ActionEvent event) {
        // Here you would handle saving the bio text
        String bioText = bioTextArea.getText();
        // For example, save the bio text to a file or database
        JOptionPane.showMessageDialog(this, "Caption saved: " + bioText);
    }

}
