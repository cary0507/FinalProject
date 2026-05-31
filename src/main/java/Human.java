import java.io.Serializable;

public class Human extends Entity implements Serializable {
    private int jobId;

    /**
     * Initializes the entity with its position, hitbox dimensions, and movement parameters.
     * Humans are spawned as unemployed fugitives
     *
     * @param x            the initial x-coordinate of the entity
     * @param y            the initial y-coordinate of the entity
     * @param hitboxWidth  the width of the entity's hitbox
     * @param hitboxHeight the height of the entity's hitbox
     * @param maxSpeed     the maximum speed the entity can reach
     * @param acceleration the acceleration magnitude of the entity
     *
     */
    public Human(int x, int y, int hitboxWidth, int hitboxHeight, double maxSpeed, double acceleration) {
        super(x, y, hitboxWidth, hitboxHeight, maxSpeed, acceleration);
        this.jobId = GamePanel.UNEMPLOYED_ID;
    }


}
