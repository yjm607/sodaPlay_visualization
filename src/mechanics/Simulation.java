package mechanics;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import drawings.Drawable;
import drawings.Mass;


/**
 * Simulates objects moving around in a bounded environment.
 * 
 * @author Robert C. Duvall
 */
public class Simulation {
    private List<Mass> myMasses;
    private List<Drawable> myDrawings;
    private Canvas myContainer;

    /**
     * Create a Canvas with the given size.
     */
    public Simulation (Canvas container) {
        myMasses = new ArrayList<Mass>();
        myDrawings = new ArrayList<Drawable>();
        myContainer = container;
    }

    public void add (Drawable drawing) {
        myDrawings.add(drawing);
    }
    
    public void add (Mass mass) {
        myMasses.add(mass);
        myDrawings.add(mass);
    }

    /**
     * Paint all shapes on the canvas.
     * else 
     * @param pen used to paint shape on the screen
     */
    public void paint (Graphics2D pen) {
        for (Drawable d : myDrawings) {
            d.paint(pen);
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
        for (Drawable d : myDrawings) {
            d.update(this, dt);
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
