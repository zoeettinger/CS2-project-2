import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A small character that can move around the screen and lose lives on collision.
 */
public class Sprite implements Actor, KeyListener {

    private int xpos;
    private int ypos;
    private int dx;
    private int dy;
    private final int speed = 10;           // movement speed per step
    private Image image;
    private int lives = 3;                  // starting lives

    // track which arrow keys are held down
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    /**
     * Reads a sprite from a png file.
     *
     * @param path location of image file
     * @param xpos initial X coordinate
     * @param ypos initial Y coordinate
     */
    public Sprite(String path, int xpos, int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, xpos, ypos, null);
    }

    @Override
    public void step() {
        xpos += dx;
        ypos += dy;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
        }
        updateVelocity();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
        }
        updateVelocity();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }

    private void updateVelocity() {
        int rawDX = 0;
        int rawDY = 0;
        if (leftPressed)  rawDX -= 1;
        if (rightPressed) rawDX += 1;
        if (upPressed)    rawDY -= 1;
        if (downPressed)  rawDY += 1;
//
//        dx = rawDX * speed;
//        dy = rawDY * speed;

        // normalize so diagonal isn't faster
        double len = Math.hypot(rawDX, rawDY);  // same as sqrt(dx*dx+dy*dy)
        if (len > 0) {
            dx = (int) Math.round(rawDX / len * speed);
            dy = (int) Math.round(rawDY / len * speed);
        } else {
            dx = 0;
            dy = 0;
        }
    }


    public Rectangle getBounds() {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        int padX = 15;   // 12px padding on left/right (was 8 before)
        int padY = 15;   // 12px padding on top/bottom (was 8 before)

        return new Rectangle(
                xpos + padX,
                ypos + padY,
                w - 2 * padX,
                h - 2 * padY
        );
    }


    @Override
    public boolean collides_with(Actor actor) {
        Rectangle me = getBounds();

        if (actor instanceof DrawablePolygon) {
            Rectangle polyBox = (actor instanceof MovingPolygon)
                    ? ((MovingPolygon) actor).getWorldBounds()
                    : ((DrawablePolygon) actor).getBounds();
            return me.intersects(polyBox);
        }

        if (actor instanceof Sprite) {
            return me.intersects(((Sprite) actor).getBounds());
        }

        return false;
    }

    /** Lose one life when hit */
    @Override
    public void onCollision(Actor other) {
        lives--;
    }

    /** Returns remaining lives ? */
    public int getLives() {
        return lives;
    }

    /** resets lives to 3 */
    public void resetLives() {
        this.lives = 3;
    }
}
