public class UpgradableStruct extends ContainerStruct {
    public int level;
    public final String[] LEVEL_IMG_PATH;
    public int princeLvlUp;

    public UpgradableStruct(int x, int y, String[] levelImgPath, GameData.ID id, int[][] relativePos) {
        super(x, y, 0, 0, 0, id, levelImgPath[0], relativePos);
        this.LEVEL_IMG_PATH = levelImgPath;
        this.level = 0;
        this.princeLvlUp = 1;
    }

    public void levelUp(int newWidth, int newHeight) {
        if (this.level >= LEVEL_IMG_PATH.length) {
            return;
        }
        this.level++;
        this.princeLvlUp *= 3;
        this.maxHP = 5 + 5 * (this.level - 1);
        this.curHP = this.maxHP;
        this.width = newWidth;
        this.height = newHeight;
        this.imagePath = this.LEVEL_IMG_PATH[this.level];
    }
}
