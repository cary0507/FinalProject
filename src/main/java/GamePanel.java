/*
* Author: Cary
* Description:
* */

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel implements  Runnable{

    final int PANEL_WIDTH = 1200;
    final int PANEL_HEIGHT = 800;
    final int HORIZON = PANEL_HEIGHT - 200;

    KeyHandler keyboard = new KeyHandler();
    Thread gameThread;

    public GamePanel() {
        // Set the preferred size of the panel
        this.setPreferredSize(new java.awt.Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(java.awt.Color.WHITE);
        this.setDoubleBuffered(true);  // Smoother rendering
        this.addKeyListener(keyboard);  // Add key detection
        this.setFocusable(true);  // Focus on this game panel
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null) {
            // Update game state
            update();
            // Refresh background
            repaint();
        }
    }

    public void update() {

    }

}
