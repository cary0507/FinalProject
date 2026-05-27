public class Mountable extends Entity {
    private boolean isMounted;  // Whether the mount is being ridden or on the ground
    private String[] animePaths;  // How it looks when mount is on the ground
    private String iconPath;  // How it looks when mount being ridden

    public Mountable(int x, int y, double maxVelocity, int hitboxWidth, int hitboxHeight) {
        super(x, y, maxVelocity, hitboxWidth, hitboxHeight);
        this.isMounted = false;
    }

    public boolean isMounted() {
        return this.isMounted;
    }

    public void releaseMount() {
        this.isMounted = false;
    }
}
