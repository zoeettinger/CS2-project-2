import java.awt.*;

public class MovingPolygon extends RegularPolygon implements Actor {
    float posx = 0;
    float posy = 0;
    private float dx = 0;
    private float dy = 0;

    /**
     * Constructs a moving polygon, given the number of sides.
     *
     * @param nsides the number of sides
     */
    public MovingPolygon(int nsides) {
        super(nsides);
    }

    /**
     * Constructs a moving polygon, given the number of sides and the radius.
     *
     * @param nsides the number of sides
     * @param radius from center to vertex
     */
    public MovingPolygon(int nsides, int radius) {
        super(nsides, radius);
    }

    /**
     * Constructs a moving polygon, given the number of sides, the radius, and
     * fill color.
     *
     * @param nsides the number of sides
     * @param radius from center to vertex
     * @param color  initial fill color
     */
    public MovingPolygon(int nsides, int radius, Color color) {
        super(nsides, radius, color);
    }

    /**
     * Constructs a moving polygon with full customization.
     *
     * @param nsides the number of sides
     * @param radius from center to vertex
     * @param color  initial fill color
     * @param posx   initial x position
     * @param posy   initial y position
     * @param dx     initial x velocity
     * @param dy     initial y velocity
     */
    public MovingPolygon(int nsides, int radius, Color color, float posx, float posy, float dx, float dy) {
        super(nsides, radius, color);
        this.posx = posx;
        this.posy = posy;
        this.dx = dx;
        this.dy = dy;
    }


    /**
     * Updates the position of the polygon during each time step.
     * Reverses direction if it reaches the edge of the Drawing.
     *
     * @param width  the width of the Drawing
     * @param height the height of the Drawing
     */
    public void step(int width, int height) {
        posx += dx;
        posy += dy;

        // Reverse direction if it hits the edges
        if (posx < 0 || posx > width) {
            dx = -dx;
        }
        if (posy < 0 || posy > height) {
            dy = -dy;
        }

        // Update the polygon's position
        translate((int) posx, (int) posy);
    }

}
