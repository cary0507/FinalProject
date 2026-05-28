public class Player {
    // Anchors the crown the player's head
    private final int HEAD_OFFSET_X = 5;
    private final int HEAD_OFFSET_Y = -5;
    private MoneyBag moneyBag;
    private Mountable mount;
    private Item crown;
    public int x;
    public int y;
    private int playerWidth;
    private int playerHeight;
    private String imagePath;

    /**
     * Initializes the player with crown anchored to the head
     * */
    public Player(MoneyBag moneyBag, Mountable mount, int playerHeight, int playerWidth, String imagePath) {
        this.moneyBag = moneyBag;
        this.mount = mount;
        // Anchor the player to the center of the mount, and the crown to the player's head
        this.x = (int) mount.x + mount.getHitboxWidth() / 2 - playerWidth / 2;
        this.y = (int) mount.x + mount.getHitboxHeight() / 2 + playerHeight / 2;
        this.playerWidth = playerWidth;
        this.playerHeight = playerHeight;
        int crownX = this.x + HEAD_OFFSET_X;
        int crownY = this.y + HEAD_OFFSET_Y;
        this.crown = new Item(crownX, crownY, 10, 10, 5, 0);
        this.imagePath = imagePath;
    }

    /**
     * Fix the player's position to follow the mount, and the crown's position to follow the player's head.
     * */
    public void update() {
        // Update the player's position to follow the mount
        this.x = (int) mount.x + mount.getHitboxWidth() / 2 - playerWidth / 2;
        this.y = (int) mount.x + mount.getHitboxHeight() / 2 + playerHeight / 2;
        // Update the crown's position to follow the player's head
        int crownX = this.x + HEAD_OFFSET_X;
        int crownY = this.y + HEAD_OFFSET_Y;
        crown.x = crownX;
        crown.y = crownY;
    }
}