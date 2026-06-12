import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MoneyBag implements Serializable {
    private final ArrayList<PickedItem> coins;
    public int capacity;
    public String[] imagePaths;
    public int dropX;
    public int dropY;
    public GamePanel gamePanel;
    public int imageIndex;

    /**
     * Initializes the money bag with a specified capacity and an empty list of coins.
     *
     * @param capacity the maximum number of coins the money bag can hold
     * */
    public MoneyBag(int capacity, int dropX, int dropY, GamePanel gamePanel) {
        this.capacity = capacity;
        this.coins = new ArrayList<>(capacity);
        this.dropX = dropX;
        this.dropY = dropY;
        this.gamePanel = gamePanel;
        this.imagePaths = GameData.moneyBagImg;
        imageIndex = 0;
    }

    /**
     * Adds a coin to the bag and has a 50% chance to drop it to the water when reached max capacity
     *
     * @param coin the coin to be added
     * */
    public void addCoin(Projectile coin) {
        Random randGen = new Random();
        final int DROP_CHANCE = 2;  // 1/2 = 50% chance

        if (coin.data.getId() == GameData.ItemID.COIN) {
            coins.add(coin.getPicked(coin.data));
            // Check when hits max capacity
            if (coins.size() > capacity) {
                int chance = randGen.nextInt(DROP_CHANCE - 1 + 1) + 1;  // nextInt(high - low + 1) + low
                if (chance == 1) {
                    Projectile droppedCoin = tossCoin();
                    droppedCoin.isOutOfBound = true;
                }
            }
        }
    }

    /**
     * Toss one coin from the bag
     *
     * @return the coin that is removed
     * */
    public Projectile tossCoin() {
        if (!coins.isEmpty()) {
            PickedItem selectCoin = coins.remove(coins.size() - 1);
            // Convert into a projectile
            Projectile tossedCoin = selectCoin.toss(dropX, dropY, 20, gamePanel);
            tossedCoin.setImagesFromPaths(tossedCoin.data.thrownImgPath, tossedCoin.data.thrownImgPath);
            tossedCoin.setMotionValues(
                    0, -3 * GamePanel.SCALE_PIXEL,   // Throws the coin upwards
                    0, GameData.GRAVITY,
                    0, false
            );
            return tossedCoin;
        }
        return null;  // No coins to toss
    }

    /**
     * Draws the bag icon with specific capacity size to the right corner of the screen
     * */
    public void render(Graphics2D g2d) {
        int screenRight = GamePanel.PANEL_WIDTH - 20 * GamePanel.SCALE_PIXEL;
        int screenTop = 20 * GamePanel.SCALE_PIXEL;
        BufferedImage img;
        if (coins.isEmpty()) {
            imageIndex = 0;
        } else if (coins.size() <= 5) {
            imageIndex = 1;
        } else {
            imageIndex = 2;
        }
        img = GameData.pathToImage(imagePaths[imageIndex]);
        int scaledWidth = img.getWidth() * GamePanel.SCALE_PIXEL * 2;
        int scaledHeight = img.getHeight() * GamePanel.SCALE_PIXEL * 2;
        g2d.drawImage(img, screenRight, screenTop, scaledWidth, scaledHeight, null);
    }
}
