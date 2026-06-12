import java.util.Random;

public class Human extends Entity {
    public GameData.JobID id;
    public Structure habitat;
    public MoneyBag moneyBag;
    public PickedItem equipping;
    public int wonderFrame;

    /**
     * Initializes the entity with its position, hitbox dimensions, and movement parameters.
     * Humans are spawned as unemployed fugitives
     */
    public Human(int x, int y, double maxSpeed, Structure habitat,
                 GamePanel gamePanel) {
        super(x, y, maxSpeed, gamePanel);
        this.id = GameData.JobID.FUGITIVE;
        this.habitat = habitat;
        wonderFrame = 0;
        moneyBag = new MoneyBag(1, x, y, gamePanel);
        setImagesFromPaths(GameData.humanImgL,  GameData.humanImgR);
    }

    public void wander() {
        if (wonderFrame <= 0) return;
        if (isFacingLeft) {
            x -= 1;
        } else {
            x += 1;
        }
    }

    public void goHme() {
        if (GameData.getDist(this, this.habitat) >= 50 * GamePanel.SCALE_PIXEL) {
            if (this.x < this.habitat.x) {
                this.isFacingLeft = false;
                this.x += (int) this.maxSpeed;
            } else  {
                this.isFacingLeft = true;
                this.x -= (int) this.maxSpeed;
            }
        }
    }

    @Override
    public void update() {
        imgIndex = id.ordinal();
        // Different job behaves differently
        switch(id) {
            case FUGITIVE:
                moneyBag.capacity = 1;
                break;
            case VILLAGER:
                moneyBag.capacity = 2;
                break;
            case FARMER:
                moneyBag.capacity = 9;
                break;
            case ARCHER:
                moneyBag.capacity = 11;
                break;
        }
    }
}
