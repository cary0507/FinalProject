import java.io.Serializable;

public class Camera implements Serializable {
    public int x;
    public int y;
    public int width;
    public int height;
    public int deadZoneLength;
    GamePanel gamePanel;

    /**
     * Initialize the Camera object
     *
     * @param gamePanel the screen that is being tracked by the camera
     * @param x left of the camera
     * @param y top of the camera
     * @param deadZoneLength the length of the dead zone (Main target will never appear inside)
     * */
    public Camera(GamePanel gamePanel, int x, int y, int deadZoneLength) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.width = gamePanel.getWidth();
        this.height = gamePanel.getHeight();
        this.deadZoneLength = deadZoneLength;
    }

    /**
     * Converts a game X-coordinate to a screen X-coordinate based on the camera's position.
     * @param actualX the X-coordinate in the game world
     * @return the X-coordinate on the screen
     */
    public int convertX(int actualX) {
        return actualX - x;
    }

    /**
     * Converts a game Y-coordinate to a screen Y-coordinate based on the camera's position.
     * @param actualY the Y-coordinate in the game world
     * @return the Y-coordinate on the screen
     */
    public int convertY(int actualY) {
        return actualY - y;
    }

    public boolean isInFreeZone(int actualX, int actualY) {
        int right = x + width;
        int bottom = y + height;
        int freeLeft = x + deadZoneLength;
        int freeTop = y + deadZoneLength;
        int freeRight = right - deadZoneLength;
        int freeBottom = bottom - deadZoneLength;
        return false;
    }
}
