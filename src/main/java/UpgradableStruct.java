public class UpgradableStruct extends ContainerStruct {
    public int level;
    public final String[] LEVEL_IMG_PATH;
    public int priceLvlUp;

    public UpgradableStruct(int x, int y, String[] levelImgPath, GameData.ID id, int[][] relativePos) {
        super(x, y, 0, 0, 0, id, relativePos);
        LEVEL_IMG_PATH = levelImgPath;
        level = 0;
        priceLvlUp = 1;
    }

    public void levelUp(int newWidth, int newHeight) {
        if (this.level >= LEVEL_IMG_PATH.length) {
            return;
        }
        level++;
        priceLvlUp *= 3;
        maxHP = 5 + 5 * (this.level - 1);
        curHP = this.maxHP;
        width = newWidth;
        height = newHeight;
        setImage(this.LEVEL_IMG_PATH[this.level]);
    }
}
