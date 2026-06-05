import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class MoneyBag implements Serializable {
    private final ArrayList<Item> coins;
    public final int capacity;
    public BufferedImage image;

    public MoneyBag(int capacity) {
        this.capacity = capacity;
        this.coins = new ArrayList<Item>(capacity);
    }

    public void setImage(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCoin(Item coin) {
        if (coin.id == GameData.ID.COIN_ID) {
            coins.add(coin);
        }
    }

    public Item tossCoin() {
        if (!coins.isEmpty()) {
            Item coin = coins.remove(coins.size() - 1);
            coin.hasPicked = false;
            return coin;
        }
        return null;  // No coins to toss
    }
}
