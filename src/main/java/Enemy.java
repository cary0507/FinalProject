public class Enemy extends Entity {
    public GameData.ID id;
    public int damage;
    public int dmgCooldown;  // Time in milliseconds between attacks
    public int curCooldown;

    public Enemy(int x, int y, int hitboxWidth, int hitboxHeight, double maxSpeed, String imagePath, int damage,
                 GameData.ID id, int dmgCooldown) {
        super(x, y, hitboxWidth, hitboxHeight, maxSpeed, imagePath);
        this.id = id;
        this.damage = damage;
        this.dmgCooldown = dmgCooldown;
        this.curCooldown = 0;
    }
}
