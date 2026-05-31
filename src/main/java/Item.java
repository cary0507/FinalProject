import java.io.Serializable;

public class Item extends Entity implements Serializable {
    public boolean hasPicked;  // Whether the item is being picked up or on the ground
    private int id;

    public Item(int x, int y, int hitboxWidth, int hitboxHeight, double maxSpeed, double acceleration, int id) {
        super(x, y, hitboxWidth, hitboxHeight, maxSpeed, acceleration);
        this.hasPicked = false;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public Item clone() {
        Item cloned = new Item(
                this.x, this.y, this.getHitboxWidth(), this.getHitboxHeight(), this.getMaxSpeed(),
                this.acceleration, this.id
        );
        cloned.hasPicked = this.hasPicked;
        cloned.id = this.id;
        return cloned;
    }
}
