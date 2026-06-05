import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class MoneyBag implements Serializable {
    private final ArrayList<Item> coins;
    public final int capacity;
    public BufferedImage image;
    public int dropX;
    public int dropY;

    /**
     * Initializes the money bag with a specified capacity and an empty list of coins.
     *
     * @param capacity the maximum number of coins the money bag can hold
     * */
    public MoneyBag(int capacity, int dropX, int dropY) {
        this.capacity = capacity;
        this.coins = new ArrayList<Item>(capacity);
        this.dropX = dropX;
        this.dropY = dropY;
    }

    /**
     * Sets the image of the money bag based on the provided file path.
     *
     * @param path the image path
     * */
    public void setImage(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a coin to the bag and has a 50% chance to drop it to the water when reached max capacity
     *
     * @param coin the coin to be added
     * */
    public void addCoin(Item coin) {
        if (coin.id == GameData.ID.COIN_ID) {
            if (coins.size() < capacity) {
                coins.add(coin);
            } else {

            }
        }
    }

    /**
     * Toss one coin from the bag
     *
     * @return the coin that is removed
     * */
    public Item tossCoin() {
        if (!coins.isEmpty()) {
            Item coin = coins.remove(coins.size() - 1);
            coin.hasPicked = false;
            return coin;
        }
        return null;  // No coins to toss
    }
}
