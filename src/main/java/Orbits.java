import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * <a href="https://www.desmos.com/calculator/yj0hqvmoor">Experiment by myself</a>
 * */
public class Orbits {
    int x, y;  // The left top corner of the orbiting object
    int width, height;
    int centerX, centerY;  // Orbit's location
    BufferedImage image;
    int trackWidth, trackHeight;
    int trackCenterX, trackCenterY;  // Trajectory's location
    int halfTrackTime;  // Frames it takes to complete half a period

    public Orbits(int trackWidth, int trackHeight, int trackCenterX, int trackCenterY, int halfTrackTime) {
        this.trackWidth = trackWidth;
        this.trackHeight = trackHeight;
        this.trackCenterX = trackCenterX;
        this.trackCenterY = trackCenterY;
        this.halfTrackTime = halfTrackTime;
    }

    public void setImageFromPath(String imgPath) {
        image = GameData.pathToImage(imgPath);
        width = image.getWidth() * GamePanel.SCALE_PIXEL;
        height = image.getHeight() * GamePanel.SCALE_PIXEL;
    }

    /**
     * Uses trig functions to calculate the x and y values of the object
     * Formula: f(x)=amplitude * sin(k(x-p))+q
     * */
    public void update(int passedFrame) {
        double kVal = Math.PI / halfTrackTime;  // Calculates k value
        int amplitudeWidth = trackWidth / 2;
        int amplitudeHeight = trackHeight / 2;
        centerX = (int) Math.floor(
                -amplitudeWidth * Math.cos(kVal * passedFrame) + trackCenterX
        );
        centerY = (int) Math.floor(  // Windows y-axis is reflected, therefore - amplitude
                -amplitudeHeight * Math.sin(kVal * passedFrame) + trackCenterY
        );
    }

    /**
     * Draws the orbiting object on screen with absolute location
     * */
    public void render(Graphics2D g2d) {
        if (image == null) {
            return;
        }
        x = centerX - width / 2;
        y = centerY - height / 2;
        g2d.drawImage(image, x, y, width, height, null);
    }

    /**
     * Draws the orbiting object as an oval shape
     * */
    public void render(Graphics2D g2d, int width, int height, Color color) {
        g2d.setColor(color);
        x = centerX - width / 2;
        y = centerY - height / 2;
        g2d.fillOval(x, y, width, height);
    }
}
