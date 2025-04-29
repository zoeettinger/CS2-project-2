import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * An Actor that tracks the mouse cursor and draws a follower circle.
 * Reacts to both movement and dragging events.
 */
public class MoveActor implements Actor, MouseMotionListener {
    private int x, y;
    private final int radius;
    private final Color color;


    public MoveActor(int radius, Color color) {
        this.radius = radius;
        this.color = color; // TODO: EDIT SO LESS TRANSPARENCY
        this.x = 0;
        this.y = 0;
    }

    @Override
    public void draw(Graphics g) {
        // draw a semi-transparent circle following the mouse
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 128));
        g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        g2d.dispose();
    }

    @Override
    public void step() {
        // no per-frame logic
    }

    @Override
    public boolean collides_with(Actor actor) {
        return false;
    }

    @Override
    public void onCollision(Actor other) {
        // no collision logic
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // update position while dragging
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // update position on any mouse move
        x = e.getX();
        y = e.getY();
    }
}
