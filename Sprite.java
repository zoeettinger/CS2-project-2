import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
/**
 * A small character that can move around the screen.
 */
public class Sprite implements Actor, KeyListener {

    private int xpos;
    private int ypos;
    private int dx;
    private int dy;
    private Image image;

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
                dy = -5;
                break;
            case KeyEvent.VK_DOWN:
                dy = +5;
                break;
            case KeyEvent.VK_LEFT:
                dx = -5;
                break;
            case KeyEvent.VK_RIGHT:
                dx = +5;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                dy = 0;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                dx = 0;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    public Rectangle getBounds() {
        return new Rectangle(
                xpos, ypos,
                image.getWidth(null),
                image.getHeight(null));
    }

    @Override
    public boolean collides_with(Actor actor) {

        Rectangle me = getBounds();          // my hit-box

        // ----- polygon targets -----
        if (actor instanceof DrawablePolygon) {
            Rectangle polyBox =
                    (actor instanceof MovingPolygon)      // moving?
                            ? ((MovingPolygon) actor).getBounds()
                            : ((DrawablePolygon) actor).getBounds();
            return me.intersects(polyBox);
        }

        // ----- another sprite -----
        if (actor instanceof Sprite) {
            return me.intersects(((Sprite) actor).getBounds());
        }

        return false;   // unknown actor type
    }
}
