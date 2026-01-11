package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // ================= BACKGROUND =================
    BufferedImage background;
    int bgY1 = 0;
    int bgY2;
    int bgSpeed = 2;

    // ================= PLAYER =================
    int playerX = 500;
    int playerY = 600;
    int playerWidth = 64;
    int playerHeight = 64;
    int playerSpeed = 10;

    int maxHealth = 3;
    int playerHealth = maxHealth;

    // ================= PLAYER ANIMATION =================
    BufferedImage[] playerFrames;
    int frameIndex = 0;
    int frameDelay = 0;

    // ================= EXPLOSION =================
    BufferedImage[] explosionFrames;
    ArrayList<Explosion> explosions = new ArrayList<>();

    // ================= ENEMY ANIMATION =================
    BufferedImage[] enemyFrames;

    // ================= GAME OBJECTS =================
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();

    // ================= INPUT =================
    boolean leftPressed = false;
    boolean rightPressed = false;
    boolean spacePressed = false;

    // ================= GAME UTIL =================
    Timer timer;
    Random random = new Random();
    int score = 0;
    boolean gameOver = false;

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);

        loadBackground();
        loadPlayerSprites();
        loadExplosionSprites();
        loadEnemySprites();

        bgY2 = -background.getHeight();

        timer = new Timer(16, this);
        timer.start();
    }

    // ================= LOAD BACKGROUND =================
    private void loadBackground() {
        try {
            background = ImageIO.read(new File("assets/background/bg.png"));
        } catch (Exception e) {
            System.out.println("Background image not found!");
        }
    }

    // ================= LOAD PLAYER SPRITES =================
    private void loadPlayerSprites() {
        try {
            playerFrames = new BufferedImage[3];
            playerFrames[0] = ImageIO.read(new File("assets/player/p1.png"));
            playerFrames[1] = ImageIO.read(new File("assets/player/p2.png"));
            playerFrames[2] = ImageIO.read(new File("assets/player/p3.png"));
        } catch (Exception e) {
            System.out.println("Player sprites not found!");
        }
    }

    // ================= LOAD EXPLOSION SPRITES =================
    private void loadExplosionSprites() {
        try {
            explosionFrames = new BufferedImage[4];
            explosionFrames[0] = ImageIO.read(new File("assets/explosion/e1.png"));
            explosionFrames[1] = ImageIO.read(new File("assets/explosion/e2.png"));
            explosionFrames[2] = ImageIO.read(new File("assets/explosion/e3.png"));
            explosionFrames[3] = ImageIO.read(new File("assets/explosion/e4.png"));
        } catch (Exception e) {
            System.out.println("Explosion sprites not found!");
        }
    }

    // ================= LOAD ENEMY SPRITES =================
    private void loadEnemySprites() {
        try {
            enemyFrames = new BufferedImage[3];
            enemyFrames[0] = ImageIO.read(new File("assets/enemy/en1.png"));
            enemyFrames[1] = ImageIO.read(new File("assets/enemy/en2.png"));
            enemyFrames[2] = ImageIO.read(new File("assets/enemy/en3.png"));
        } catch (Exception e) {
            System.out.println("Enemy sprites not found!");
        }
    }

    // ================= DRAW =================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 🌌 Draw scrolling background
        g.drawImage(background, 0, bgY1, getWidth(), background.getHeight(), null);
        g.drawImage(background, 0, bgY2, getWidth(), background.getHeight(), null);

        drawAnimatedPlayer(g);

        for (Bullet b : bullets) b.draw(g);
        for (Enemy e : enemies) e.draw(g);
        for (Explosion ex : explosions) ex.draw(g);

        drawHealthBar(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 90);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("GAME OVER",
                    getWidth() / 2 - 180,
                    getHeight() / 2);
        }
    }

    // ================= PLAYER ANIMATION =================
    private void drawAnimatedPlayer(Graphics g) {

        if (leftPressed || rightPressed) {
            frameDelay++;
            if (frameDelay >= 18) {
                frameIndex = (frameIndex + 1) % playerFrames.length;
                if (frameIndex == 0) frameIndex = 1;
                frameDelay = 0;
            }
        } else {
            frameIndex = 0;
        }

        g.drawImage(playerFrames[frameIndex],
                playerX, playerY,
                playerWidth, playerHeight,
                null);
    }

    // ================= HEALTH BAR =================
    private void drawHealthBar(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect(20, 30, 200, 20);
        g.setColor(Color.RED);
        g.fillRect(20, 30,
                (int) (200 * (playerHealth / (double) maxHealth)), 20);
    }

    // ================= GAME LOOP =================
    @Override
    public void actionPerformed(ActionEvent e) {

        if (gameOver) return;

        // 🌌 Scroll background
        bgY1 += bgSpeed;
        bgY2 += bgSpeed;

        if (bgY1 >= background.getHeight()) {
            bgY1 = bgY2 - background.getHeight();
        }
        if (bgY2 >= background.getHeight()) {
            bgY2 = bgY1 - background.getHeight();
        }

        if (leftPressed) playerX -= playerSpeed;
        if (rightPressed) playerX += playerSpeed;

        playerX = Math.max(0, Math.min(getWidth() - playerWidth, playerX));

        for (Bullet b : bullets) b.move();
        for (Enemy en : enemies) en.move();

        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if (explosions.get(i).isFinished()) {
                explosions.remove(i);
                i--;
            }
        }

        if (spacePressed) {
            bullets.add(new Bullet(playerX + 30, playerY));
            SoundPlayer.playSound("assets/sound/shoot.wav");
            spacePressed = false;
        }

        if (random.nextInt(100) < 2) {
            enemies.add(new Enemy(
                    random.nextInt(getWidth() - 40),
                    0,
                    enemyFrames
            ));
        }

        checkCollisions();
        repaint();
    }

    // ================= COLLISIONS =================
    private void checkCollisions() {

        Rectangle playerRect =
                new Rectangle(playerX, playerY, playerWidth, playerHeight);

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            for (int j = 0; j < enemies.size(); j++) {
                Enemy en = enemies.get(j);
                if (b.getBounds().intersects(en.getBounds())) {

                    explosions.add(
                            new Explosion(en.x, en.y, explosionFrames)
                    );
                    SoundPlayer.playSound("assets/sound/explosion.wav");

                    bullets.remove(i);
                    enemies.remove(j);
                    score += 10;
                    return;
                }
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy en = enemies.get(i);
            if (playerRect.intersects(en.getBounds())) {
                enemies.remove(i);
                playerHealth--;
                if (playerHealth <= 0) {
                    gameOver = true;
                    timer.stop();
                }
                return;
            }
        }
    }

    // ================= INPUT =================
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) spacePressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) spacePressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
