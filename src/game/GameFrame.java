package game;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Mass Action Arena");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel();
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
