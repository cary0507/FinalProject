public class ContainerStruct extends Structure {
    public Entity[] containing;
    public int[][] relativePos;  // Stored x and y positions to anchor entities to the structure

    /**
     * Initializes the structure with its position, hitbox dimensions, health points, and the entities it contains.
     * */
    public ContainerStruct(int x, int y, int maxHP, GameData.StructureID id, int[][] relativePos, GamePanel gamePanel) {
        super(x, y, maxHP, id, gamePanel);
        this.relativePos = relativePos;
        if (relativePos != null) {
            this.containing = new Entity[relativePos.length];
        } else {
            this.containing = null;
        }
    }

    /**
     * Adds an Entity to an empty slot of "containing"
     * */
    public void addEntity(Entity entity) {
        // Loop through the array to find an empty spot
        for (int i = 0; i < containing.length; i++) {
            if (containing[i] == null) {
                containing[i] = entity;
            }
        }
    }

    /**
     * Anchors the contained ordered entities to the structure by updating their positions based on the structure's
     * current position and their relative positions.
     * Precondition: none
     * Postcondition: anchors the entities to the respective coordinates, or do nothing
     * */
    public void anchorEntities() {
        if (containing == null || relativePos == null) {
            return;
        }
        int x = 0, y = 1;  // Too lazy to create a Coordinate class
        for (int i = 0; i < containing.length; i++) {
            if (containing[i] != null) {
                containing[i].x = this.x + relativePos[i][x];
                containing[i].y = this.y + relativePos[i][y];
            }
        }
    }

    /**
     * Take away the Entity at the specified index from the structure, returning a duplicate of it and setting the
     * original reference to null.
     *
     * @param index the index of the entity to be taken away from the structure
     * */
    public Entity takeAway(int index) {
        Entity taken = this.containing[index].duplicate();
        containing[index] = null;
        return taken;
    }

    @Override
    public void update() {
        super.update();
        anchorEntities();
    }
}
