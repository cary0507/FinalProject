public class Player extends Entity {
    // Offsets to nchors the crown the player's head
    public final int HEAD_OFFSET_X = 15;
    public final int HEAD_OFFSET_Y = -15;
    private Mountable mount;
    private Item crown;
    KeyHandler keyInput;
    GamePanel gamePanel;

    /**
     * Initializes the player with crown anchored to the head and riding a default horse
     * @param keyHandler the key handler to control the player
     * @param gamePanel the game panel to render the player
     */
    public Player(KeyHandler keyHandler, GamePanel gamePanel, String imagePath) {
        super(0, 0, 10, 10, 0, imagePath);
        this.keyInput = keyHandler;
        this.gamePanel = gamePanel;
    }

    public void setDefaultValues() {

    }

    public void setMount(Mountable mount) {}

    public void update() {
        
    }

    /**
     * Fix the player's position to follow the mount, and the crown's position to follow the player's head.
     * */
    public void anchorsMount(Mountable targetMount) {
        // Update the player's position to follow the mount
        x = (int) mount.x + targetMount.hitboxWidth / 2 - hitboxWidth / 2;
        y = (int) mount.x + targetMount.hitboxHeight / 2 + hitboxHeight / 2;
        // Update the crown's position to follow the player's head
        int crownX = x + HEAD_OFFSET_X;
        int crownY = y + HEAD_OFFSET_Y;
        crown.x = crownX;
        crown.y = crownY;
    }

    public void swapMount(Mountable newMount) {
        mount.isMounted = false;  // Dismount the current mount
        mount = newMount;         // Switch to the new mount
        mount.isMounted = true;   // Mount the new mount
        anchorsMount(newMount);   // Update the player's position to follow the new mount
    }

    public Item loseCrown() {
        crown.hasPicked = false;
        Item lostCrown = crown.duplicate();  // Create a copy of the crown to drop on the ground
        crown = null;
        return lostCrown;
    }
}