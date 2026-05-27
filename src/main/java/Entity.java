public class Entity {
    // Scalar magnitudes
    private double acceleration;
    private double speed = 0;
    private double maxSpeed;
    private double facingDir;
    // Vector components
    private double accX;
    private double accY;
    private double velX;
    private double velY;
    private int x;
    private int y;
    private int hitboxWidth;
    private int hitboxHeight;
    // Stores image file paths
    private String[] animePaths;  // How it looks when it is moving
    private String iconPath;  // How it looks when at stationary

    public Entity(int x, int y, int hitboxWidth, int hitboxHeight, double maxSpeed, double acceleration) {
        this.x = x;
        this.y = y;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void updateAccVectors() {
        this.accX = Math.cos(this.facingDir) * this.acceleration;
        this.accY = Math.sin(this.facingDir) * this.acceleration;
    }

    public void updateVelVectors() {
        double newVelX = this.velX + this.accX;
        double newVelY = this.velY + this.accY;
        double velMagnitude = Math.sqrt(newVelX * newVelX + newVelY * newVelY);
        if (velMagnitude > this.maxSpeed) {
            double maxVelX = Math.cos(this.facingDir) * this.maxSpeed;
            double maxVelY = Math.sin(this.facingDir) * this.maxSpeed;
            this.velX = maxVelX;
            this.velY = maxVelY;
        } else {
            this.velX = newVelX;
            this.velY = newVelY;
        }
    }
}
