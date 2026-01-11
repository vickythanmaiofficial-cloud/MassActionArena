package game;

import java.awt.Color;
import java.awt.Graphics;

public class Player {

    int x = 370;
    int y = 500;
    int width = 50;
    int height = 20;

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, width, height);
    }
}
