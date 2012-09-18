import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


/**
 * Simulates objects moving around in a bounded environment.
 * 
 * @author Robert C. Duvall
 */
public class Simulation {
    private List<Mass> myMasses;
    private List<Spring> mySprings;
    private Canvas myContainer;

    /**
     * Create a Canvas with the given size.
     */
    public Simulation (Canvas container) {
        myMasses = new ArrayList<Mass>();
        mySprings = new ArrayList<Spring>();
        myContainer = container;
    }

    public void add (Mass mass) {
        myMasses.add(mass);
    }

    public void add (Spring spring) {
        mySprings.add(spring);
    }

    /**
     * Paint all shapes on the canvas.
     * else 
     * @param pen used to paint shape on the screen
     */
    public void paint (Graphics2D pen) {
        for (Spring s : mySprings) {
            s.paint(pen);
        }
        for (Mass m : myMasses) {
            m.paint(pen);
        }
    }

    /**
     * Called by each step of timer, multiple times per second.
     * 
     * This should update the state of the animated shapes by just
     * a little so they appear to move over time.
     */
    public void update (double dt) {
        updateMovers(dt);
    }

    void updateMovers (double dt) {
        for (Spring s : mySprings) {
            s.update(this, dt);
        }
        for (Mass m : myMasses) {
            m.update(this, dt);
        }
    }

    /**
     * Returns size (in pixels) of the game area.
     */
    public Dimension getSize () {
        return myContainer.getSize();
    }

    public Mass getMass (int id) {
        for (Mass m : myMasses) {
            if (m.match(id)) return m;
        }
        return null;
    }
}
