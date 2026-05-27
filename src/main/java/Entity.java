public class Entity {
    private double acceleration;
    private double velocity;
    private double maxVelocity;
    private int x;
    private int y;
    private int hitboxWidth;
    private int hitboxHeight;
    // Stores image file paths
    private String[] animePaths;  // How it looks when it is moving
    private String iconPath;  // How it looks when at stationary

    public Entity(int x, int y, double maxVelocity, int hitboxWidth, int hitboxHeight) {
        this.x = x;
        this.y = y;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.maxVelocity = maxVelocity;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

}
