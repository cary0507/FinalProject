/*
* Author: Cary
* Reference: https://www.youtube.com/watch?v=oPzPpUcDiYY&list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq&index=8
* */

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {
    // Environment settings
    public static final int PANEL_WIDTH = 1200;
    public static final int PANEL_HEIGHT = 840;
    public static final int HORIZON = PANEL_HEIGHT - 300;
    public static final int SCALE_PIXEL = 4;  // Pixel sizes of a square tile (Learned from YouTube)
    public int leftBound;
    public int rightBound;
    // Game state
    public boolean paused = false;
    // Pause menu UI
    Rectangle resumeBtn;
    Rectangle restartBtn;
    Rectangle saveQuitBtn;
    private final Font titleFont = new Font("Algerian", Font.BOLD, 48);
    private final Font textFont = new Font("Algerian", Font.BOLD, 28);
    // Time
    final int SEC_IN_NANO = 1_000_000_000;  // 1_000_000_000 nanosecond = 1 second
    public static final int FPS = 60;
    // Sky colors
    public final int MAX_BLUE = 255;
    public final int MIN_BLUE = 52;
    public final int RED_DIFF = 52;
    public final int GREEN_DIFF = 22;
    // Scale settings
    KeyHandler keyboard = new KeyHandler();
    Thread gameThread;
    // Game data setting
    public GameData gameData;

    /**
     * Initializes the game panel with its dimensions, background color, and key listener.
     * */
    public GamePanel() throws IOException, ClassNotFoundException {
        // Set the preferred size of the panel (From YouTube)
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);  // Smoother rendering
        this.addKeyListener(keyboard);  // Add key detection
        this.setFocusable(true);  // Focus on this game panel
        // Setup saved game data
        gameData = new GameData(keyboard, this);
        // Load the game
        gameData = (GameData) gameData.loadGame("src/main/resources/serialized/save.ser");
        gameData.resetTransient(this, keyboard);
        leftBound = gameData.leftBound;
        rightBound = gameData.rightBound;

        // Setup pause menu buttons
        int btnW = 260;
        int btnH = 44;
        int yGap = 80;
        int btnTop = PANEL_HEIGHT / 2 + 20;
        int btnLeft = 20;
        resumeBtn = new Rectangle(btnLeft, btnTop, btnW, btnH);
        restartBtn = new Rectangle(btnLeft, btnTop + btnH + yGap, btnW, btnH);
        saveQuitBtn = new Rectangle(btnLeft, btnTop + (btnH + yGap) * 2, btnW, btnH);

        // Mouse listener for pause menu buttons
        addMouseListener(new PauseMenuMouseListener(this));
    }

    public void startGameThread() {
        // Learned from YouTube
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * The main game loop that updates the game state and renders the graphics at a consistent frame rate.
     * */
    @Override
    public void run() {
        double drawInterval = (double) SEC_IN_NANO / FPS;  // Time per frame in nanoseconds
        double deltaTime = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null) {
            // Calculate the time elapsed since the last frame (Learned from YouTube)
            currentTime = System.nanoTime();
            deltaTime += (currentTime - lastTime) / drawInterval;  // Accumulate the time in terms of frames
            lastTime = currentTime;

            if (deltaTime >= 1) {
                // Toggle pause when ESC is pressed once
                if (keyboard.escPressedOnce) {
                    paused = !paused;
                    // consume the one-shot toggle
                    keyboard.escPressedOnce = false;
                }

                // Update game properties (skip when paused)
                if (!paused) {
                    // Store the current frame
                    gameData.framePassed++;
                    if (gameData.framePassed >= gameData.NEXT_DAY_FRAME) {
                        gameData.framePassed = 0;
                    }
                    update();
                }
                // Resets keys after the update
                keyboard.resetTiggerKey();
                // Draws the updated game (Learned from YouTube video)
                repaint();
                deltaTime -= 1;  // Reset the delta time for the next frame
            }
        }
    }

    /**
     * Handles the interactions between projectiles and other objects
     * */
    public void updateProjectiles () {
        for (int i = 0; i < gameData.allProjectiles.size(); i++) {
            Projectile projectile = gameData.allProjectiles.get(i);
            projectile.update();
            // Check if this projectile is a coin
            if (Objects.requireNonNull(projectile.data.getId()) == GameData.ItemID.COIN) {
                // The player will pick up the coin if they collide and the coin is not in pickup delay
                if (GameData.isInside(projectile, gameData.player.mount)
                        && projectile.data.curPickFrame >= projectile.data.maxPickDelay) {  // Prevents instant pick

                    gameData.player.moneyBag.addCoin(projectile, "player");
                    gameData.allProjectiles.remove(projectile);
                    return;
                }
                // Humans can pick up coin only if they have space for their money bag
                for (Human human : gameData.allHumans) {
                    if (GameData.isInside(human, projectile)
                            && human.moneyBag.capacity > human.moneyBag.numCoins
                            // Can only pick up coins thrown by player
                            && projectile.data.owner != null  // Order matters
                            && projectile.data.owner.equalsIgnoreCase("player")) {

                        human.moneyBag.addCoin(projectile, "human");
                        gameData.allProjectiles.remove(projectile);
                        return;
                    }
                }
                // When the coin interacts with an upgradable structure
                for (UpgradableStruct upgradeStruct : gameData.allUpgradable) {
                    if (GameData.isInside(upgradeStruct, projectile)
                            // Check if the object can further level up
                            && upgradeStruct.level < upgradeStruct.maxLevel
                            && projectile.data.owner != null
                            && projectile.data.owner.equalsIgnoreCase("player")) {

                        if (upgradeStruct.id == GameData.StructureID.TOWN_CENTER
                                // Current maximum level is the town center level
                                || upgradeStruct.level < gameData.townCenter.level) {

                            // Pay coin first may be safer
                            gameData.allProjectiles.remove(projectile);
                            // Calculate new hp
                            int newHP = upgradeStruct.maxHP + 5 + 2 * upgradeStruct.level;
                            upgradeStruct.levelUp(newHP);
                            return;
                        }
                    }
                }
                // When the coin interacts with an item structure
                for (ContainerStruct containerStruct : gameData.allContainers) {
                    if (GameData.isInside(containerStruct, projectile)
                            && containerStruct.numItems < containerStruct.containing.length
                            && projectile.data.owner != null
                            && projectile.data.owner.equalsIgnoreCase("player")) {

                        // Pay coin first may be safer
                        gameData.allProjectiles.remove(projectile);
                        // Generate an item to the shelf
                        switch (containerStruct.id) {
                            case BOW_SHELF:
                                // Sets up data
                                ItemData bowItemData = new ItemData(
                                        GameData.ItemID.BOW, GameData.bowItemImg, true
                                );
                                // Sets up item
                                Projectile bowItem = new Projectile(
                                        0, 0, GameData.UNIVERSAL_TOP_SPEED,
                                        this, bowItemData
                                );
                                // Add item to data
                                gameData.allProjectiles.add(bowItem);
                                // Add item to shelf
                                containerStruct.addEntity(bowItem);
                                break;
                            case SICKLE_SHELF:
                                // Sets up data
                                ItemData sickleItemData = new ItemData(
                                        GameData.ItemID.SICKLE, GameData.sickleItemImg, true
                                );
                                // Sets up item
                                Projectile sickleItem = new Projectile(
                                        0, 0, GameData.UNIVERSAL_TOP_SPEED,
                                        this, sickleItemData
                                );
                                // Add item to data
                                gameData.allProjectiles.add(sickleItem);
                                // Add item to shelf
                                containerStruct.addEntity(sickleItem);
                                break;
                        }
                        return;
                    }
                }
            } else if (Objects.requireNonNull(projectile.data.getId()) == GameData.ItemID.BOW) {
                // Villagers without a job can pick up
                for (Human human : gameData.allHumans) {

                }
            }
        }
    }

    /**
     * Updates the game state, including player movement, enemy behavior, and other game logic.
     * */
    public void update() {
        Random rand = new Random();
        // Determines the stage of the day
        boolean isNight;
        // Updates background
        gameData.changeSkyColor(MAX_BLUE, MIN_BLUE, RED_DIFF, GREEN_DIFF);  // Sky
        if (gameData.framePassed < gameData.NIGHT_FRANE) {  // Noon
            // Sun's orbit
            gameData.sun.update(gameData.framePassed);
            isNight = false;
        } else if (gameData.framePassed == gameData.NIGHT_FRANE) {  // At Night
            // ChobjOute a random portal to spawn enemy
            int bound = gameData.allPortals.size();
            int choice = rand.nextInt(bound);
            isNight = true;
            gameData.allEnemies.addAll(gameData.allPortals.get(choice).generateEnemies());
        } else {  // Night
            // Moon's orbit
            int sinceNight = gameData.framePassed - gameData.NIGHT_FRANE;
            gameData.moon.update(sinceNight);
            isNight = true;
        }

        // Update player data
        gameData.player.update();

        // Update each mount
        for (int i = 0; i < gameData.allMounts.size(); i++) {
            Mountable mount = gameData.allMounts.get(i);
            mount.update();
        }

        // Update each structure
        for (int i = 0; i < gameData.allContainers.size(); i++) {
            ContainerStruct container = gameData.allContainers.get(i);
            container.update();
        }
        for (int i = 0; i < gameData.allUpgradable.size(); i++) {
            UpgradableStruct upgradeStruct = gameData.allUpgradable.get(i);
            upgradeStruct.update();
        }

        updateProjectiles();

        // Update each human
        for (int i = 0; i < gameData.allHumans.size(); i++) {
            Human human = gameData.allHumans.get(i);
            // Set the habitate
            switch (human.id) {
                case FUGITIVE:
                case VILLAGER:
                case FARMER:   // Fall through
                    human.habitat = gameData.townCenter;  // They all live in town center
                    break;
            }
            human.update(isNight);
        }

        // Update each enemy
        for (int i = 0; i < gameData.allEnemies.size(); i++) {
            Enemy enemy = gameData.allEnemies.get(i);
            enemy.update(gameData.player);
        }

        // Update each chunk
        for (int i = 0; i < gameData.allChunks.size(); i++) {
            Chunk chunk = gameData.allChunks.get(i);
            chunk.update(gameData.player);
        }
        // The order of the update matters because if camera is updated first, it will take some delay for the camera
        // to focus on the main objects again
        gameData.camera.focusOn(gameData.player.mount);  // Update camera
    }

    /**
     * Draws a button with customized name
     * */
    private void renderButton(Graphics2D g2d, Rectangle r, String text) {
        // Background
        Color fill = new Color(120, 71, 55);
        g2d.setColor(fill);
        g2d.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
        // Boarder
        Color border = new Color(122, 115, 84);
        g2d.setColor(border);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRoundRect(r.x, r.y, r.width, r.height, 8, 8);
        // Name of the button
        g2d.setFont(textFont);
        FontMetrics fm = g2d.getFontMetrics();
        int labelX = r.x + (r.width - fm.stringWidth(text)) / 2;
        int labelY = r.y + (r.height - fm.getHeight()) / 2 + 30;
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, labelX, labelY);
    }

    /**
     * Draws the paused menu and title
     * */
    public void renderPauseMenu(Graphics2D g2d) {
        // translucent background
        Composite oldComp = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));  // Blurring effect
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        g2d.setComposite(oldComp);

        // Draw menu panel
        int menuW = 300;
        int menuH = PANEL_HEIGHT;
        int menuX = 0;
        int menuY = 0;
        g2d.setColor(new Color(94, 49, 31));
        g2d.fillRoundRect(menuX, menuY, menuW, menuH, 12, 12);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(menuX, menuY, menuW, menuH, 12, 12);

        // Draw title
        g2d.setFont(titleFont);
        g2d.setColor(new Color(227, 190, 98));
        FontMetrics fontMetrics = g2d.getFontMetrics();
        String title = "Paused";
        int textX = menuW / 2 - fontMetrics.stringWidth(title) / 2;
        int textY = menuY + 40 * SCALE_PIXEL;
        g2d.drawString(title, textX, textY);

        // Draw buttons
        renderButton(g2d, resumeBtn, "Resume");
        renderButton(g2d, restartBtn, "Restart");
        renderButton(g2d, saveQuitBtn, "Save & Quit");
    }

    /**
     * Render the components with override codes from the original method
     * */
    @Override  // Learned from YouTube video
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Renders background
        this.setBackground(gameData.skyColor);
        if (gameData.framePassed <= gameData.NIGHT_FRANE) {
            gameData.sun.render(g2d, 40 * SCALE_PIXEL, 40 * SCALE_PIXEL, Color.WHITE);
        } else {
            Color moonColor = new Color(133, 147, 154);
            gameData.moon.render(g2d, 35 * SCALE_PIXEL, 35 * SCALE_PIXEL, moonColor);
        }

        // Draw river front
        Color riverColor = new Color(93, 112, 106);
        g2d.setColor(riverColor);
        g2d.fillRect(0, HORIZON + 120, GamePanel.PANEL_WIDTH, 50);

        // Renders all structures
        for (ContainerStruct container : gameData.allContainers) {
            container.render(g2d, gameData.camera);
            if (GameData.isInside(container, gameData.player.mount)
                    // If shelf is not full
                    && container.numItems < container.containing.length) {

                container.renderHint(g2d, gameData.camera);  // Hints the player to toss a coin
            }
        }
        for (UpgradableStruct upgradeStruct : gameData.allUpgradable) {
            upgradeStruct.render(g2d, gameData.camera);
            if (GameData.isInside(upgradeStruct, gameData.player.mount)
                    // If not at max level
                    && upgradeStruct.level < upgradeStruct.maxLevel) {

                if (upgradeStruct.id == GameData.StructureID.TOWN_CENTER
                        // Current maximum level is the town center level
                        || upgradeStruct.level < gameData.townCenter.level) {
                    upgradeStruct.renderHint(g2d, gameData.camera);
                }
            }
        }

        // Renders all NPC
        for (Human human : gameData.allHumans) {
            human.render(g2d, gameData.camera);
        }

        // Render all enemies
        for (Enemy enemy: gameData.allEnemies) {
            enemy.render(g2d, gameData.camera);
        }

        // Render the projectiles
        for (Projectile projectile : gameData.allProjectiles) {
            projectile.render(g2d, gameData.camera);
        }

        // Render all mounts
        for (Mountable mount : gameData.allMounts) {
            mount.render(g2d, gameData.camera);
        }

        // Render the player
        gameData.player.render(g2d, gameData.camera);

        // Render second layer of river
        g2d.setColor(riverColor);
        g2d.fillRect(0, HORIZON + 170, GamePanel.PANEL_WIDTH, 140);
        // Render chunks
        for (Chunk chunk : gameData.allChunks) {
            chunk.render(g2d, gameData.camera);
        }

        // Render all portals
        for (int i = 0; i < gameData.allPortals.size(); i++) {
            Portal portal = gameData.allPortals.get(i);
            portal.render(g2d, gameData.camera);
        }
        // If paused, render pause menu overlay and buttons on top
        if (paused) {
            renderPauseMenu(g2d);
        }

        // Dispose of the graphics context to free up resources
        g2d.dispose();
    }
}
