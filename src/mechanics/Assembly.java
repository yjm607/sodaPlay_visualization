package mechanics;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import drawings.Drawable;
import drawings.Mass;


/**
 * Creates Assembly object that contains Drawable objects.
 * 
 * @author Jei Min Yoo & Volodymyr Zavidovych
 * 
 */
public class Assembly {
    private List<Drawable> myDrawings;
    private Simulation mySimulation;

    /**
     * constructs an Assembly object
     * 
     * @param sim used for communication with simulation object
     */
    public Assembly (Simulation sim) {
        myDrawings = new ArrayList<Drawable>();
        mySimulation = sim;
    }

    /**
     * adds a Drawable object in the assembly
     * 
     * @param drawing used to include Drawable object
     */
    public void add (Drawable drawing) {
        myDrawings.add(drawing);
    }

    /**
     * draws all Drawable objects in the assembly
     * 
     * @param pen used to draw Drawable objects
     */
    public void paint (Graphics2D pen) {
        for (Drawable d : myDrawings) {
            d.paint(pen);
        }
    }

    /**
     * updates all Drawable objects in the assembly
     * 
     * @param dt determines elapsed time
     */
    public void updateMovers (double dt) {
        for (Drawable d : myDrawings) {
            d.update(mySimulation, this, dt);
        }
    }

    /**
     * gets a Drawable object in the assembly
     * 
     * @param id used to determine which Drawable object to select
     */
    public Drawable getDrawable (int id) {
        for (Drawable d : myDrawings) {
            if (d.match(id)) {
                return d;
            }
        }
        return null;
    }

    /**
     * gets the list of drawings
     */
    public List<Drawable> getMyDrawings () {
        return myDrawings;
    }

    /**
     * gets the nearest mass in the assembly to a point
     * 
     * @param point used to determine the nearest distance
     */
    public Mass getNearestMass (Point point) {
        Mass nearestMass = null;
        double minDistance = Math.max(mySimulation.getSize().getHeight(),
                mySimulation.getSize().getWidth());
        for (Drawable d : myDrawings) {
            if ("mass".equals(d.getClassName())) {
                double distance = Force.distanceBetween(point,
                        ((Mass) d).getCenter());
                if (distance < minDistance) {
                    nearestMass = (Mass) d;
                    minDistance = distance;
                }
            }
        }
        return nearestMass;
    }

}
