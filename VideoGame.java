import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Color;

/**
 * Example game with a sprite that moves around the screen.
 */
public class VideoGame implements ActionListener {

    private Drawing drawing;
    private Toolkit toolkit;
    private MovingPolygon mpoly;

    /**
     * Set up the drawing and window frame.
     */
    public VideoGame() {
        Sprite sprite = new Sprite("face-smile.png", 25, 150);
        drawing = new Drawing(800, 600);
        drawing.add(sprite);
        drawing.addKeyListener(sprite);
        drawing.setFocusable(true);

        mpoly = new MovingPolygon(3, 50, Color.GREEN, 100, 80, 50, 50);
        drawing.add(mpoly);
        mpoly.translate(100, 80);


        JFrame frame = new JFrame("Video Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(drawing);
        frame.pack();
        frame.setVisible(true);
        toolkit = frame.getToolkit();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        drawing.step();
        mpoly.step(50, 50);
        mpoly.draw(drawing.getGraphics());
        toolkit.sync();
    }

    /**
     * Create and start the timer.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        VideoGame game = new VideoGame();
        Timer timer = new Timer(33, game);
        timer.start();
    }

}
