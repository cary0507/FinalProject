public class Projectile extends Entity {
    // Scalar magnitudes
    public double acceleration;
    public double speed = 0;
    public double facingDir;
    // Vector components
    private double accX;
    private double accY;
    private double velX;
    private double velY;

    public Projectile(int x, int y, int hitboxWidth, int hitboxHeight, double maxSpeed, double acceleration) {
        super(x, y, hitboxWidth, hitboxHeight, maxSpeed);
        this.acceleration = acceleration;
    }

    /**
     * Calculate the acceleration vector components based on the facing direction and acceleration magnitude.
     * */
    public void updateAccVectors() {
        this.accX = Math.cos(this.facingDir) * this.acceleration;
        this.accY = Math.sin(this.facingDir) * this.acceleration;
    }

    /**
     * Calculate the velocity vector components based on the current velocity, acceleration, and facing direction.
     * */
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

    /**
     * Update the position of the entity based on its velocity vector components.
     * */
    public void getDisplacement() {
        this.x += (int) this.velX;
        this.y += (int) this.velY;
    }
}
