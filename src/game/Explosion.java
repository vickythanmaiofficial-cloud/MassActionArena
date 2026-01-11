package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Explosion {

    // Position where explosion appears
    int x, y;

    // Size of explosion on screen
    int width = 64;
    int height = 64;

    // Animation frames
    BufferedImage[] frames;
    int frameIndex = 0;
    int frameDelay = 0;

    // Marks when animation is finished
    boolean finished = false;

    // Constructor
    public Explosion(int x, int y, BufferedImage[] frames) {
        this.x = x;
        this.y = y;
        this.frames = frames;
    }

    // Called every frame to advance animation
    public void update() {
        frameDelay++;

        if (frameDelay >= 6) {   // controls explosion speed
            frameIndex++;
            frameDelay = 0;

            if (frameIndex >= frames.length) {
                finished = true;
            }
        }
    }

    // Draw current explosion frame
    public void draw(Graphics g) {
        if (!finished) {
            g.drawImage(
                frames[frameIndex],
                x,
                y,
                width,
                height,
                null
            );
        }
    }

    // Used by GamePanel to remove explosion
    public boolean isFinished() {
        return finished;
    }
}
