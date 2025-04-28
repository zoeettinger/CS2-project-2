import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.*;


// WIN CONDITION: NO WIN CONDITION: TRY TO GO FOR HIGH SCORE


public class VideoGame implements ActionListener, KeyListener {
    private final Sprite sprite;
    private final Drawing drawing;
    private final Toolkit toolkit;

    // ← instead of one mpoly, keep a list
    private final List<MovingPolygon> mpolygons = new ArrayList<>();
    private final Random rand = new Random();

    // we'll use this to pace our spawns
    private int ticks = 0;

    private boolean gameOver = false;

    // pace up enemy speed over time
    private float difficulty = 1.0f;

    private int score = 0;


    public VideoGame() {
        sprite = new Sprite("face-smile.png", 25, 150);

        // create a custom Drawing that also shows lives / game over
        drawing = new Drawing(800, 600) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setFont(g.getFont().deriveFont(18f));
                g.drawString("Lives: " + sprite.getLives(), 10, 20);
                g.drawString("Score: " + score, 10, 40);

                if (gameOver) {
                    String msg = "GAME OVER";
                    String finalScoreMsg = "Final Score: " + score;
                    String restartMsg = "Press R to restart";
                    int gameOverWidth = g.getFontMetrics().stringWidth(msg);
                    int finalScoreWidth = g.getFontMetrics().stringWidth(finalScoreMsg);
                    int restartWidth = g.getFontMetrics().stringWidth(restartMsg);
                    g.drawString(msg, (getWidth() - gameOverWidth) / 2, getHeight() / 2);
                    g.drawString(finalScoreMsg, (getWidth() - finalScoreWidth) / 2, getHeight() / 2 + 20);
                    g.drawString(restartMsg, (getWidth() - restartWidth) / 2, getHeight() / 2 + 40);
                }
            }
        };

        drawing.add(sprite);
        drawing.addKeyListener(sprite);
        drawing.addKeyListener(this);
        drawing.setFocusable(true);

        int initial_polygons = rand.nextInt(8) + 7;
        for (int i = 0; i < initial_polygons; i++) {
            spawnPolygon();
        }
//        for (int i = 0; i < 5; i++) {
//            spawnPolygon();
//        }

        ClickActor clickActor = new ClickActor(drawing, mpolygons, sprite, delta -> this.score += delta, 5000000); // we can pass mpolygons in because java passes these by refs
        drawing.add(clickActor);
        drawing.addMouseListener(clickActor);



        JFrame frame = new JFrame("Video Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(drawing);
        frame.pack();
        frame.setVisible(true);
        toolkit = frame.getToolkit();
    }

    public static void main(String[] args) {
        VideoGame game = new VideoGame();
        new Timer(33, game).start();
    }

    /**
     * create a new MovingPolygon with random properties
     */
    private void spawnPolygon() {
        int sides  = rand.nextInt(5) + 3;    // 3–7 sides
        int radius = rand.nextInt(20) + 20;  // radius 20–40

        // get the sprite’s center point
        Rectangle playerBox = sprite.getBounds();
        float px = playerBox.x + playerBox.width  / 2f;
        float py = playerBox.y + playerBox.height / 2f;

        // enforce at least (radius + half-sprite + buffer) pixels away
        float buffer  = 17.5f;
        float minDist = radius + Math.max(playerBox.width, playerBox.height)/2f + buffer;

        float x, y;
        do {
            x = rand.nextFloat() * drawing.getWidth();
            y = rand.nextFloat() * drawing.getHeight();
        } while (Math.hypot(x - px, y - py) < minDist);

        float speed = rand.nextFloat() * (3 + (difficulty / 2)) + 1;  // 1–4± px per step
        float dx    = speed * (rand.nextBoolean() ? 1 : -1);
        float dy    = speed * (rand.nextBoolean() ? 1 : -1);

        Color c = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());

        MovingPolygon mp = new MovingPolygon(sides, radius, c, x, y, dx, dy);
        mpolygons.add(mp);
        drawing.add(mp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            drawing.step();

            // every 120 ticks (~4 seconds at 33 ms/tick) spawn another
            if (++ticks % 120 == 0) {
                spawnPolygon();
            }

            score++;

            // check collisions & removal
            // iterate over a copy so we can remove safely
            for (MovingPolygon mp : new ArrayList<>(mpolygons)) {
                if (mp.isAlive() && sprite.collides_with(mp)) {
                    sprite.onCollision(mp);
                    mp.onCollision(sprite);
                    score -= 50; // put here to satisfy score that updates with collisions
                }
                if (!mp.isAlive()) {
                    drawing.remove(mp);
                    mpolygons.remove(mp);
                }
            }

            // if out of lives, stop the game
            if (sprite.getLives() <= 0) {
                gameOver = true;
            }
        }

        drawing.repaint();
        toolkit.sync();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
            resetGame();
        }
    }

    /** Clear everything and start fresh */
    private void resetGame() {
        // 1) reset flags & counters
        gameOver = false;
        score    = 0;
        ticks    = 0;

        // 2) reset the sprite
        sprite.resetLives();
//        sprite.setPosition(START_X, START_Y); // don't reset pos

        // 3) remove existing polygons
        for (MovingPolygon mp : new ArrayList<>(mpolygons)) {
            drawing.remove(mp);
        }
        mpolygons.clear();

        // 4) spawn initial batch again
        int initial_polygons = rand.nextInt(8) + 7;
        for (int i = 0; i < initial_polygons; i++) {
            spawnPolygon();
        }
    }

    @Override public void keyReleased(KeyEvent e) { }
    @Override public void keyTyped(KeyEvent e)    { }

    public void incrementScore(int amount) {
        score += amount;
    }
}
