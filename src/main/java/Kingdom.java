/*
* Author: Cary
* Description:
* */

import javax.swing.JFrame;

public class Kingdom{
    public static void main(String[] args) {
        final int WIDTH = 1200;
        final int HEIGHT = 800;
        JFrame mainScreen = new JFrame("Kingdom");
        mainScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainScreen.setSize(WIDTH, HEIGHT);
        mainScreen.setResizable(false);
        mainScreen.setVisible(true);
    }
}
