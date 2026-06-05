import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Entity implements Serializable {
    public final double maxSpeed;
    // The pixels on the screen has to be a whole number, hence the int type.
    public int x;
    public int y;
    public final int hitboxWidth;
    public final int hitboxHeight;
    // Stores image files
    public int imgIndex;  // Determines which image to use for animation
    public boolean isFacingLeft;    // Since it's a 2D game and there's only left and right
                                    // This value only affects which image to use, not the actual movement direction
    public ArrayList<BufferedImage> leftImages;
    public ArrayList<BufferedImage> rightImages;

    /**
     * Initializes the entity with its position, hitbox dimensions, and movement parameters.
     * @param x the initial x-coordinate of the entity
     * @param y the initial y-coordinate of the entity
     * @param hitboxWidth the width of the entity's hitbox
     * @param hitboxHeight the height of the entity's hitbox
     * @param maxSpeed the maximum speed the entity can reach
     * */
    public Entity(int x, int y, int hitboxWidth, int hitboxHeight, double maxSpeed) {
        this.x = x;
        this.y = y;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.maxSpeed = maxSpeed;
        imgIndex = 0;
        // initialize fields (avoid shadowing local variables)
        this.leftImages = new ArrayList<>();
        this.rightImages = new ArrayList<>();
    }

    public void setUpImages(ArrayList<BufferedImage> leftImages, ArrayList<BufferedImage> rightImages) {
        this.leftImages = leftImages;
        this.rightImages = rightImages;
    }

    /**
     * Load images from file paths and populate leftImages and rightImages lists.
     * If any image fails to load it will be skipped.
     * @param leftImagePaths  paths to left-facing images
     * @param rightImagePaths paths to right-facing images
     */
    public void setUpImagesFromPaths(ArrayList<String> leftImagePaths, ArrayList<String> rightImagePaths) {
        this.leftImages = new ArrayList<>();
        this.rightImages = new ArrayList<>();

        if (leftImagePaths != null) {
            for (String path : leftImagePaths) {
                if (path == null) continue;
                try {
                    BufferedImage img = ImageIO.read(new File(path));
                    if (img != null) this.leftImages.add(img);
                } catch (IOException e) {
                    System.err.println("Failed to load left image: " + path + " -> " + e.getMessage());
                }
            }
        }

        if (rightImagePaths != null) {
            for (String path : rightImagePaths) {
                if (path == null) continue;
                try {
                    BufferedImage img = ImageIO.read(new File(path));
                    if (img != null) this.rightImages.add(img);
                } catch (IOException e) {
                    System.err.println("Failed to load right image: " + path + " -> " + e.getMessage());
                }
            }
        }

        // ensure imgIndex is valid
        if (imgIndex < 0) imgIndex = 0;
        if (isFacingLeft && this.leftImages.size() == 0) imgIndex = 0;
        if (!isFacingLeft && this.rightImages.size() == 0) imgIndex = 0;
    }

    public Entity duplicate() {
        Entity duplicate = new Entity(x, y, hitboxWidth, hitboxHeight, maxSpeed);
        duplicate.setUpImages(leftImages, rightImages);
        return duplicate;
    }

    public void update() {

    }

    public void render(Graphics2D g2) {
        BufferedImage img;
        if (isFacingLeft) {
            img = leftImages.get(imgIndex);
        } else {
            img = rightImages.get(imgIndex);
        }
        g2.drawImage(img, x, y, null);
    }
}
