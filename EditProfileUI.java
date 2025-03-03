
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;

public class EditProfileUI extends JFrame {
      final int WIDTH = 300;
      final int HEIGHT = 500;
      final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
      final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images
    private JPanel contentPanel; // Panel to display the image grid or the clicked image
    private JPanel buttonPanel;
    private JPanel bioFieldPanel;
    private User currentUser;
    private JButton submitButton, cancelButton;
    private JButton btnUploadPhoto;
    private JPanel photoUploadPanel;
    // private JPanel fieldsPanel;
    private JTextField txtBio;

    boolean imgUploaded = false;
    boolean bioUpdated = false;

    private final String credentialsFilePath = "data/credentials.txt";
    private final String profilePhotoStoragePath = "img/storage/profile/";

    public EditProfileUI(User user) {
        currentUser = user;
        setTitle("Edit Profile");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    public void initializeUI() {
        add(contentPanel(), BorderLayout.NORTH);
        add(bioFieldPanel(), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);
    }

    private Component bioFieldPanel() {
        bioFieldPanel = new JPanel();
        txtBio = new JTextField("Bio", 10);
        bioFieldPanel.add(Box.createVerticalStrut(10));
        bioFieldPanel.add(txtBio);

        return bioFieldPanel;
    }

    @SuppressWarnings("unused")
    private void displayImage(ImageIcon imageIcon) {
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        // JButton backButton = new JButton("Back");
        // backButton.addActionListener(e -> {
        // getContentPane().removeAll(); // Remove all components from the frame
        // initializeUI(); // Re-initialize the UI
        // });
        // contentPanel.add(backButton, BorderLayout.SOUTH);

        // revalidate();
        // repaint();
    }

    private Component photoUploadPanel() {
        photoUploadPanel = new JPanel();
        photoUploadPanel.add(uploadPhoto());

        return photoUploadPanel;
    }

    private Component buttonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(cancelButton());
        buttonPanel.add(submitButton());

        return buttonPanel;
    }

    private Component contentPanel() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 2, 5, 5)); // Grid layout for image grid
        contentPanel.add(imgaeIcon());
        contentPanel.add(photoUploadPanel());

        return contentPanel;
    }

    private Component cancelButton() {
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                InstagramProfileUI igProfile = new InstagramProfileUI(currentUser);
                igProfile.setVisible(true);
            }
        });

        return cancelButton;
    }

    private Component submitButton() {
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(imgUploaded || bioUpdated){
                    saveNewBio(currentUser, credentialsFilePath);
                dispose();
                InstagramProfileUI igProfile = new InstagramProfileUI(currentUser);
                igProfile.setVisible(true);
                }   
            }

        });
        return submitButton;
    }

    private Component imgaeIcon() {
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("img/storage/profile/" + currentUser.getUsername() + ".png")
                .getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return profileImage;
    }

    private Component uploadPhoto() {
        btnUploadPhoto = new JButton("Upload Photo");
        btnUploadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProfilePictureUpload();
            }
        });

        return btnUploadPhoto;
    }

    private void handleProfilePictureUpload() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveProfilePicture(selectedFile, currentUser.getUsername());
        }
    }

    private void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(profilePhotoStoragePath + username + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveNewBio(User user, String newBio) {
        newBio = txtBio.getText();
       String username = user.getUsername();
        String currentBio = user.getBio();

        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials[0].equals(username) && credentials[2].equals(currentBio)) {
                    try(BufferedWriter writer = new BufferedWriter(new FileWriter(credentialsFilePath, true))){ 
                     writer.write(user.toString());
                     bioUpdated = true;
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
