import java.awt.*;

public class Player extends Entity {
    private final MoneyBag moneyBag;
    private Mountable mount;
    private Item crown;
    // Offset when facing left
    private int crownXOffsetL;
    private int crownYOffsetL;
    // Offset when facing right
    private int crownXOffsetR;
    private int crownYOffsetR;
    KeyHandler keyInput;
    GamePanel gamePanel;

    /**
     * Initializes the player with crown anchored to the head and riding a default horse
     * @param keyHandler the key handler to control the player
     * @param gamePanel the game panel to render the player
     */
    public Player(KeyHandler keyHandler, GamePanel gamePanel, Mountable mount) {
        super(0, 0, 10, 10, 0);
        this.keyInput = keyHandler;
        this.gamePanel = gamePanel;
        this.mount = mount;
        crown = new Item(0, 0, 10, 10, 5, GameData.ID.CROWN_ID);
        mount.isMounted = true;
        mount.anchorsPassenger(this);  // Anchor the player to the mount's position
        moneyBag = new MoneyBag(15);
    }

    /**
     * Anchors the crown to the player's head based on facing direction
     * */
    public void anchorsCrown() {
        if (isFacingLeft) {
            crown.x = x + crownXOffsetL;
            crown.y = y + crownYOffsetL;
        } else {
            crown.x = x + crownXOffsetR;
            crown.y = y + crownYOffsetR;
        }
    }

    public void swapMount(Mountable newMount) {
        mount.isMounted = false;            // Dismount the current mount
        mount = newMount;                   // Switch to the new mount
        mount.isMounted = true;             // Mount the new mount
        newMount.anchorsPassenger(this);    // Update the player's position to follow the new mount
    }

    public Item loseCrown() {
        crown.hasPicked = false;
        Item lostCrown = crown.duplicate();  // Create a copy of the crown to drop on the ground
        crown = null;
        return lostCrown;
    }

    @Override
    public void update() {
        if (mount == null) {
            return;
        }
        // Update player's actions based on key inputs while mounted
        if (keyInput.downPressed) {
            Item tossedCoin = moneyBag.tossCoin();
            tossedCoin.x = x;
            tossedCoin.y = y;
            tossedCoin.velX = 0;
            tossedCoin.velY = -5;                   // Toss the coin upwards
            tossedCoin.accX = 0;
            tossedCoin.accY = GameData.GRAVITY;     // Apply gravity to the tossed
            GamePanel.gameData.allItems.add(tossedCoin);
        }
        if (keyInput.leftPressed) {
            isFacingLeft = true;
            mount.isFacingLeft = true;
            mount.x += (int) mount.maxSpeed;
            mount.anchorsPassenger(this);   // Update the player's position to follow the mount
            return;                         // Prevent movement in the opposite direction if both keys are pressed
        }
        if (keyInput.rightPressed) {
            isFacingLeft = false;
            mount.isFacingLeft = false;
            mount.x -= (int) mount.maxSpeed;
            mount.anchorsPassenger(this);
        }
    }
}