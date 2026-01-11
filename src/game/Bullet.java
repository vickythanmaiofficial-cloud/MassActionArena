package game;

import java.awt.*;

public class Bullet {

    int x, y;
    int speed = 15;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        y -= speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 6, 12);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 6, 12);
    }
}
