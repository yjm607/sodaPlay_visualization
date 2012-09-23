package mechanics;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import drawings.Drawable;
import drawings.Mass;

/**
 * Simulates objects moving around in a bounded environment.
 * 
 * @author Robert C. Duvall
 */
public class Simulation {
    private List<Assembly> myAssemblies;
    private Canvas myContainer;
    private Environment myEnvironment;


    /**
     * Create a Canvas with the given size.
     */
    public Simulation (Canvas container) {
        myAssemblies = new ArrayList<Assembly>();
        myContainer = container;
        myEnvironment = new Environment(container);
    }

//    public void add(Scanner line, String type) {
//        myEnvironment.add(line, type);
//    }
    
//    public void add (Drawable drawing) {
//        myDrawings.add(drawing);
//    }
    
    public void add (Assembly assembly) {
        myAssemblies.add(assembly);
    }
    /**
     * Paint all shapes on the canvas.
     * else 
     * @param pen used to paint shape on the screen
     */
    public void paint (Graphics2D pen) {
        for (Assembly a : myAssemblies) {
            a.paint(pen);
        }
    }

    /**
     * Called by each step of timer, multiple times per second.
     * 
     * This should update the state of the animated shapes by just
     * a little so they appear to move over time.
     */
    public void update (double dt) {
        for (Assembly a : myAssemblies) {
            a.updateMovers(dt);
        }
    }

//    void updateMovers (double dt) {
//        for (Drawable d : myAssemblies) {
//            d.update(this, dt);
//        }
//    }

    /**
     * Returns size (in pixels) of the game area.
     */
    public Dimension getSize () {
        return myContainer.getSize();
    }

//    public Drawable getDrawable (int id) {
//        for (Drawable d : myDrawings) {
//            if (d.match(id)) return d;
//        }
//        return null;
//    }

    public Environment getEnvironment() {
        return myEnvironment;
    }
    
    public List<Assembly> getMyAssemblies() {
        return myAssemblies;
    }    
}
