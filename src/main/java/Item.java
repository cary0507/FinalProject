public class Item extends Entity {
    private boolean hasPicked;  // Whether the item is being picked up or on the ground

    public Item(int x, int y, double maxVelocity, int hitboxWidth, int hitboxHeight) {
        super(x, y, maxVelocity, hitboxWidth, hitboxHeight);
        this.hasPicked = false;
    }

    public boolean hasPicked() {
        return this.hasPicked;
    }

    public void releaseItem() {
        this.hasPicked = false;
    }
}
