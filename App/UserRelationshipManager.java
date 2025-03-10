import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRelationshipManager {

    private final String followersFilePath = "data/followers.txt";

    /**
     * This method is use to save a information o=upon following of a user
     * @param follower
     * @param followed
     * @throws IOException
     */
    public void followUser(String follower, String followed) throws IOException {
        if (!isAlreadyFollowing(follower, followed)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(followersFilePath, true))) {
                writer.write(follower + ":" + followed);
                writer.newLine();
            }
        }
    }

/**
 * This method checks whether a User is already following another one
 * @param follower
 * @param followed
 * @return boolean
 * @throws IOException
 */    private boolean isAlreadyFollowing(String follower, String followed) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(follower + ":" + followed)) {
                    return true;
                }
            }
        }
        return false;
    }

/**
 * This method is used to get a hser's followers
 * @param username
 * @return list of followers
 * @throws IOException
 */    public List<String> getFollowers(String username) throws IOException {
        List<String> followers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[1].equals(username)) {
                    followers.add(parts[0]);
                }
            }
        }
        return followers;
    }

/**
 * This method get the lost of followed Users
 * @param username
 * @return List of followings
 * @throws IOException
 */    public List<String> getFollowing(String username) throws IOException {
        List<String> following = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    following.add(parts[1]);
                }
            }
        }
        return following;
    }
}
