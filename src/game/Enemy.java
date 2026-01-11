package game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {

    public int x, y;
    int width = 48;
    int height = 48;
    int speed = 4;

    // ================= ENEMY ANIMATION =================
    BufferedImage[] frames;
    int frameIndex = 0;
    int frameDelay = 0;

    // Constructor
    public Enemy(int x, int y, BufferedImage[] frames) {
        this.x = x;
        this.y = y;
        this.frames = frames;
    }

    // Movement
    public void move() {
        y += speed;
    }

    // Animation + draw
    public void draw(Graphics g) {
        frameDelay++;
        if (frameDelay >= 10) {
            frameIndex = (frameIndex + 1) % frames.length;
            frameDelay = 0;
        }

        g.drawImage(
                frames[frameIndex],
                x,
                y,
                width,
                height,
                null
        );
    }

    // Collision box
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
