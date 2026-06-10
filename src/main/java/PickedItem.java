public class PickedItem {
    public final ItemData data;
    public int imgIndex;

    public PickedItem(ItemData data) {
        this.data = data;
        this.imgIndex = 0;
    }

    public Projectile toss(int x, int y, int hitboxWidth, int hitboxHeight, double maxSpeed, GamePanel gamePanel) {
        Projectile projectile =  new Projectile(x, y, hitboxWidth, hitboxHeight, maxSpeed, gamePanel);
        return projectile;
    }
}
