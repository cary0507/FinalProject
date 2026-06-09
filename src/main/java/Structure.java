import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
public class Structure extends Entity {
    public int maxHP;
    public int curHP;
    public GameData.ID id;
    public GameData.ID[] blockedID;

    /**
     * Initializes the structure with its position, dimensions, hit points, and image path.
     *
     * @param x the x-coordinate of the structure
     * @param y the y-coordinate of the structure
     * @param hitboxWidth the width of the structure
     * @param hitboxHeight the height of the structure
     * @param maxHP the maximum hit points of the structure
     * @param id the id of the object
     * @param gamePanel the screen that the object appear on
     */
    public Structure(int x, int y, int hitboxWidth, int hitboxHeight, int maxHP, GameData.ID id, GamePanel gamePanel) {
        super(x, y, hitboxWidth, hitboxHeight, 0, gamePanel);
        this.gamePanel = gamePanel;
        this.maxHP = maxHP;
        this.curHP = maxHP;
        this.id = id;
    }

    /**
     * Determines Entity with which IDs cannot go through
     * */
    public void setBlockedID(GameData.ID[] blockedID) {
        this.blockedID = blockedID;
    }

    /**
     * Drops one coin at the center of the structure when it is destroyed
     * @return an Item object representing the dropped coin, positioned at the center of the structure
     * */
    public Item rewardCoin() {
        int centerX = (int) (x + hitboxWidth / 2);
        Item coin = new Item(centerX, y, 20, 20, 6, GameData.ID.COIN_ID, gamePanel);
        return coin;
    }
}
