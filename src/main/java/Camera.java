import java.io.Serializable;

public class Camera implements Serializable {
    public int x;
    public int y;
    public int width;
    public int height;
    public int deadZoneWidth;
    public int deadZoneHeight;
    GamePanel gamePanel;

    /**
     * Initialize the Camera object
     *
     * @param gamePanel the screen that is being tracked by the camera
     * @param x left of the camera
     * @param y top of the camera
     * @param deadZoneWidth the height of the dead zone (Main target will never appear inside)
     * @param deadZoneHeight the width of the dead zone
     * */
    public Camera(GamePanel gamePanel, int x, int y, int deadZoneWidth, int deadZoneHeight) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.width = GamePanel.PANEL_WIDTH;
        this.height = GamePanel.PANEL_HEIGHT;
        this.deadZoneWidth = deadZoneWidth;
        this.deadZoneHeight = deadZoneHeight;
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

    /**
     * Warps the main focus Entity inside the free moving zone
     *
     * @param mainFocus the Entity to be focused on
     * */
    public void focusOn(Entity mainFocus) {
        // Stores calculation in variables
        // See /rough draft.png
        int right = x + this.width;
        int bottom = y + this.height;
        int freeLeft = x + deadZoneWidth;
        int freeTop = y + deadZoneHeight;
        int freeRight = right - deadZoneWidth;
        int freeBottom = bottom - deadZoneHeight;
        int mainFocusRight = mainFocus.x + mainFocus.hitboxWidth;
        int mainFocusBottom = mainFocus.y + mainFocus.hitboxHeight;
        // Check for each boundary
        if (mainFocus.x < freeLeft) {       // Out of left bound
            this.x = mainFocus.x - deadZoneWidth;
        }
        if (mainFocus.y < freeTop) {        // Out of top bound
            this.y = mainFocus.y - deadZoneHeight;
        }
        if (mainFocusRight > freeRight) {   // Out of right bound
            this.x = mainFocusRight + deadZoneWidth - this.width;
        }
        if (mainFocusBottom > freeBottom) { // Out of bottom bound
            this.y = mainFocusBottom + deadZoneHeight - this.height;
        }
    }
}
