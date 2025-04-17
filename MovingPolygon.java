import java.awt.*;

public class MovingPolygon extends RegularPolygon {
    float posx = 0;
    float posy = 0;
    float dx = 0;
    float dy = 0;

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
}
