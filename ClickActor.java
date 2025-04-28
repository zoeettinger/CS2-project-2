import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

/**
 * An Actor that draws a circle and reacts when clicked.
 */
public class ClickActor implements Actor, MouseListener {
    private int x, y, radius;
    private Color color;
    private boolean visible = true;
    private final Consumer<Integer> onClickScore;

    public ClickActor(int x, int y, int radius, Color color, Consumer<Integer> onClickScore) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.onClickScore = onClickScore;
    }

    @Override
    public void draw(Graphics g) {
        if (!visible) return;
        g.setColor(color);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public void step() {
        // no per-frame behavior
    }

    @Override
    public boolean collides_with(Actor actor) {
        // not used for collision detection
        return false;
    }

    @Override
    public void onCollision(Actor other) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        int dx = e.getX() - x;
        int dy = e.getY() - y;
        if (dx * dx + dy * dy <= radius * radius) {
            visible = !visible;
            onClickScore.accept(100);
        }
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
}
