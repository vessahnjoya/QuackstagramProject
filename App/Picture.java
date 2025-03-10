import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a picture in quackstagram, it contains manager methods
 */
public class Picture { // public to handle error in User package
    private String imagePath;
    private String caption;
    private int likesCount;
    private List<String> comments;

    /**
     * The constructorofds initializes imagepath, caption, likesCount, and comments
     * 
     * @param imagePath
     * @param caption
     */
    public Picture(String imagePath, String caption) {
        this.imagePath = imagePath;
        this.caption = caption;
        this.likesCount = 0;
        this.comments = new ArrayList<>();
    }

    /**
     * This method adds comments to an arrylist
     * 
     * @param comment
     */
    public void addComment(String comment) {
        comments.add(comment);
    }

    /**
     * This methods increments the likescount
     */
    public void like() {
        likesCount++;
    }

    /**
     * Getter method for image path
     * 
     * @return imagepath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Getter method for caption
     * 
     * @return caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Getter method for likes count
     * 
     * @return likescount
     */
    public int getLikesCount() {
        return likesCount;
    }

    /**
     * Getter methods for comments
     * 
     * @return comments
     */
    public List<String> getComments() {
        return comments;
    }
}
