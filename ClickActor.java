import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * An Actor that listens for clicks and destroys any MovingPolygon clicked on,
 * awarding points for each kill.
 */
public class ClickActor implements Actor, MouseListener {
    private final Drawing drawing;
    private final List<MovingPolygon> mpolygons;
    private final Sprite sprite;
    private final Consumer<Integer> onScore;
    private final int bonus;


    public ClickActor(Drawing drawing, List<MovingPolygon> mpolygons, Sprite sprite, Consumer<Integer> onScore, int bonus) {
        this.drawing   = drawing;
        this.mpolygons = mpolygons;
        this.sprite    = sprite;
        this.onScore   = onScore; // use consumer so we can edit score in videogame.java
        this.bonus     = bonus;
    }

    @Override
    public void draw(Graphics g) {
        // invisible actor no drawing
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
    public void onCollision(Actor other) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        // moves after animation so we shouldnt use to delete shapes
        // spent way to trying to debug this
    }

    @Override public void mousePressed(MouseEvent e)  {
        int mx = e.getX();
        int my = e.getY();
        // System.out.println("Clicked at: " + e.getX() + "," + e.getY());

        // iterate over a copy to avoid concurrent modification
        for (MovingPolygon mp : new ArrayList<>(mpolygons)) {
            // if click lands in the polygon's world bounds
            if (mp.getWorldBounds().contains(mx, my)) {
                // trigger normal collision behavior
                sprite.onCollision(mp);
                mp.onCollision(sprite);

                // remove from canvas and list
                drawing.remove(mp);
                mpolygons.remove(mp);

                // award points
                onScore.accept(bonus);
            }
        }
    }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e)  { }
    @Override public void mouseExited(MouseEvent e)   { }
}
