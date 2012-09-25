package drawings;

import java.awt.Graphics2D;
import mechanics.Assembly;
import mechanics.Simulation;


/**
 * Interface for Drawable objects.
 * 
 * @author Jei Min Yoo & Volodymyr Zavidovych
 * 
 */
public interface Drawable {

    /**
     * determines if object matches give id
     * 
     * @param id determines id to match.
     */
    boolean match (int id);

    /**
     * draws the object on screen
     * 
     * @param pen used to draw the object
     */
    void paint (Graphics2D pen);

    /**
     * updates the object's status
     * 
     * @param canvas used to draw the object
     * @param assembly used to determine applied forces
     * @param dt used to determine elapsed time
     */
    void update (Simulation canvas, Assembly assembly, double dt);

    /**
     * get the name of the object's class
     */
    String getClassName ();
}
