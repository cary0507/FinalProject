import java.io.Serializable;

public class Camera implements Serializable {
    public int x;
    public int y;
    public int width;
    public int height;

    public Camera(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Converts a game X-coordinate to a screen X-coordinate based on the camera's position.
     * @param gameX the X-coordinate in the game world
     * @return the X-coordinate on the screen
     */
    public int convertX(int gameX) {
        return gameX - x;
    }

    /**
     * Converts a game Y-coordinate to a screen Y-coordinate based on the camera's position.
     * @param gameY the Y-coordinate in the game world
     * @return the Y-coordinate on the screen
     */
    public int convertY(int gameY) {
        return gameY - y;
    }
}
