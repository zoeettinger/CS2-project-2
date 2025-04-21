public class EllipseFollowsMouse extends JPanel implements MouseListener, MouseMotionListener {

    private int ellipseX = 100;
    private int ellipseY = 100;
    private int ellipseWidth = 100;
    private int ellipseHeight = 100;
    private boolean dragging = false;
    private Color ellipseColor = Color.BLUE;

    public EllipseFollowsMouse() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setPreferredSize(new Dimension(400,400));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(ellipseColor);
        g.fillOval(ellipseX,ellipseY,ellipseWidth,ellipseHeight);
    }

    public void mousePressed(MouseEvent e){
        if(isInsideEllipse(e.getX(), e.getY())){
            dragging = true;
            ellipseColor = color.RED;
            repaint();
        }
    }

    public void mouseReleased(MouseEvent e){
        dragging = false;
        ellipseColor = color.BLUE;
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
        if(dragging){
            ellipseX = e.get(X) - ellipseWidth / 2;
            ellipseY = e.get(Y) - ellipseHeight / 2;
            repaint();
        }
    }

}