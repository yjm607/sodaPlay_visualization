package mechanics;

import drawings.Bar;
import drawings.Mass;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
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
    /**
     * default key value when no key is pressed.
     */
    public static final int NO_KEY_PRESSED = -1;
    /**
     * static value used for one second.
     */
    public static final int ONE_SECOND = 1000;
    /**
     * animate 25 times per second if possible (in milliseconds)
     */
    public static final int FRAMES_PER_SECOND = 25;
    /**
     * default muscle oscillation period
     */
    public static final double MUSCLE_OSCILLATION_PERIOD = 1.5;
    /**
     * affects the effect of forces with exponents
     */
    public static final double FORCE_DISTANCE_DIVIDER = 15;
    /**
     * walled area offset increment value
     */
    public static final int OFFSET_INCREMENT = 10;
    /**
     * angle representing direction "up"
     */
    public static final int UP_ANGLE = 270;
    /**
     * angle representing direction "down"
     */
    public static final int DOWN_ANGLE = 90;
    /**
     * angle representing direction "left"
     */
    public static final int LEFT_ANGLE = 180;
    /**
     * angle representing direction "right"
     */
    public static final int RIGHT_ANGLE = 0;

    // mouse dragging information
    private Assembly myNearestAssembly;
    private Mass myMouseMass;
    private int myMouseMassDefaultMass = -1;
    private int myMouseMassDefaultID = -1;
    private Bar myMouseBar;
    private double myMouseBarDefaultKVal = -1;

    // user's game to be animated
    private Simulation myTarget;
    private Factory myFactory;
    // drives simulation
    private Timer myTimer;
    // input state
    private int myLastKeyPressed;
    private Point myLastMousePosition;
    // only one so that it maintains user's preferences
    private static final JFileChooser ourChooser = 
            new JFileChooser(System.getProperties().getProperty("user.dir"));

    /**
     * Initializes the canvas.
     * 
     * @param size determines the size of the canvas
     */
    public Canvas (Dimension size) {
        // request component size
        setPreferredSize(size);
        // set component to receive user input
        setInputListeners();
        setFocusable(true);
        requestFocus();
        // create timer to drive the animation
        myTimer = new Timer(ONE_SECOND / FRAMES_PER_SECOND, new ActionListener() {
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
     * 
     * @param elapsedTime used for updating Simulation object's time
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
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased (MouseEvent e) {
                myLastMousePosition = e.getPoint();
                manageMouseReleased(myLastMousePosition);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged (MouseEvent e) {
                myLastMousePosition = e.getPoint();
                manageMouseDragged(myLastMousePosition);
            }
        });
    }

    private void loadModel () {
        myFactory = new Factory();
        readInput(myFactory, "chooseAssembly");
        readInput(myFactory, "chooseEnvironment");
    }

    private void readInput (final Factory factory, String prompt) {
        int response = ourChooser.showDialog(this, prompt);
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
            case KeyEvent.VK_N:
                readInput(myFactory, "Read additional assembly.");
                break;
            case KeyEvent.VK_C:
                myTarget.clearAssemblies();
                break;
            case KeyEvent.VK_V:
            case KeyEvent.VK_G:
            case KeyEvent.VK_M:
            case KeyEvent.VK_1:
            case KeyEvent.VK_2:
            case KeyEvent.VK_3:
            case KeyEvent.VK_4:
                myTarget.getEnvironment().toggleForce(keyCode);
                break;
            case KeyEvent.VK_UP:
                myTarget.changeMyWalledAreaOffset(OFFSET_INCREMENT);
                break;
            case KeyEvent.VK_DOWN:
                myTarget.changeMyWalledAreaOffset(-OFFSET_INCREMENT);
                break;
            default:
                // good style
                break;
        }
    }

    private void manageMouseDragged (Point point) {
        if (myMouseMass != null) {
            myMouseMass.setCenter(point.getX(), point.getY());
        }
        else {
            createMouseDragger(point);
        }
    }

    private void createMouseDragger (Point point) {
        List<Assembly> myAssemblies = myTarget.getMyAssemblies();
        Mass nearestMass = null;
        double minDistance = Math.max(getSize().getHeight(), getSize().getWidth());
        for (Assembly a : myAssemblies) {
            Mass localNearestMass = a.getNearestMass(point);
            double localMinDistance = point.distance(localNearestMass.getCenter());
            if (localMinDistance <= minDistance) {
                nearestMass = localNearestMass;
                myNearestAssembly = a;
                minDistance = localMinDistance;
            }
        }
        myMouseMass =
                new Mass(myMouseMassDefaultID, point.getX(), point.getY(), myMouseMassDefaultMass);
        myMouseBar = new Bar(myMouseMass, nearestMass, minDistance, myMouseBarDefaultKVal);
        myNearestAssembly.add(myMouseMass);
        myNearestAssembly.add(myMouseBar);
    }

    private void manageMouseReleased (Point point) {
        myNearestAssembly.getMyDrawings().remove(myMouseMass);
        myNearestAssembly.getMyDrawings().remove(myMouseBar);
        myMouseMass = null;
        myMouseBar = null;
    }
}
