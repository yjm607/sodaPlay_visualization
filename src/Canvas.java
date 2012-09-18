import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.Timer;


/**
 * Creates an component that is a viewer onto an animation.
 * 
 * @author Robert C. Duvall
 */
@SuppressWarnings("serial")
public class Canvas extends JComponent {
    public static final int NO_KEY_PRESSED = -1;
    // animate 25 times per second if possible (in milliseconds)
    public static final int ONE_SECOND = 1000;
    public static final int FRAMES_PER_SECOND = 25;

    // user's game to be animated
    private Simulation myTarget;
    // drives simulation
    private Timer myTimer;
    // input state
    private int myLastKeyPressed;
    private Point myLastMousePosition;
    // only one so that it maintains user's preferences
    private static final JFileChooser ourChooser = new JFileChooser(System
            .getProperties().getProperty("user.dir"));

    /**
     * Initializes the canvas.
     * 
     * @param size
     */
    public Canvas (Dimension size) {
        // request component size
        setPreferredSize(size);
        // set component to receive user input
        setInputListeners();
        setFocusable(true);
        requestFocus();
        // create timer to drive the animation
        myTimer = new Timer(ONE_SECOND / FRAMES_PER_SECOND,
                new ActionListener() {
                    @Override
                    public void actionPerformed (ActionEvent e) {
                        step((double) FRAMES_PER_SECOND / ONE_SECOND);
                    }
                });
        // initialize simulation
        myTarget = new Simulation(this);
        loadModel();
    }

    /**
     * Starts the applet's action, i.e., starts the animation.
     */
    public void start () {
        myTimer.start();
    }

    /**
     * Take one step in the animation.
     */
    public void step (double elapsedTime) {
        myTarget.update(elapsedTime);
        // indirectly causes paint to be called
        repaint();
    }

    /**
     * Stops the applet's action, i.e., the animation.
     */
    public void stop () {
        myTimer.stop();
    }

    /**
     * Returns the last key pressed by the player (or -1 if none pressed).
     * 
     * @see java.awt.event.KeyEvent
     */
    public int getLastKeyPressed () {
        return myLastKeyPressed;
    }

    /**
     * Returns the last position of the mouse in the canvas.
     */
    public Point getLastMousePosition () {
        return myLastMousePosition;
    }

    /**
     * Paint the contents of the canvas.
     * 
     * Never called by you directly, instead called by Java runtime
     * when area of screen covered by this container needs to be
     * displayed (i.e., creation, uncovering, change in status)
     * 
     * @param pen used to paint shape on the screen
     */
    @Override
    public void paintComponent (Graphics pen) {
        pen.setColor(Color.WHITE);
        pen.fillRect(0, 0, getSize().width, getSize().height);
        myTarget.paint((Graphics2D) pen);
    }

    /**
     * Create listeners that will update state based on user input.
     */
    private void setInputListeners () {
        myLastKeyPressed = NO_KEY_PRESSED;
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed (KeyEvent e) {
                myLastKeyPressed = e.getKeyCode();
                manageSimulation(myLastKeyPressed);
            }

            @Override
            public void keyReleased (KeyEvent e) {
                myLastKeyPressed = NO_KEY_PRESSED;
            }
        });
        myLastMousePosition = new Point();
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved (MouseEvent e) {
                myLastMousePosition = e.getPoint();
            }
        });
    }

    private void loadModel () {
        final Factory factory = new Factory();
        int response = ourChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            factory.loadModel(myTarget, ourChooser.getSelectedFile());
        }
    }

    private void manageSimulation (int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_SPACE:
                if (myTimer.isRunning()) {
                    stop();
                }
                else {
                    start();
                }
                break;
            case KeyEvent.VK_S:
                step((double) FRAMES_PER_SECOND / ONE_SECOND);
                break;
            case KeyEvent.VK_P:
                System.out.println(myTarget);
                break;
            default:
                // good style
                break;
        }
    }
}
