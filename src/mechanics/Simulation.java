package mechanics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


/**
 * Simulates objects moving around in a bounded environment.
 * 
 * @author Robert C. Duvall / modified: Jei Min Yoo & Volodymyr Zavidovych
 * 
 */
/**
 * @author Jei Min Yoo
 * 
 */
public class Simulation {
    private List<Assembly> myAssemblies;
    private Canvas myContainer;
    private Environment myEnvironment;
    private int myWalledAreaOffset;

    /**
     * Create a Canvas with the given size.
     * 
     * @param container used for communication with Canvas object
     */
    public Simulation (Canvas container) {
        myAssemblies = new ArrayList<Assembly>();
        myContainer = container;
        myEnvironment = new Environment(container);
        myWalledAreaOffset = 0;
    }

    /**
     * Add an assembly object to the list of assemblies.
     * 
     * @param assembly used to add to the list.
     */
    public void add (Assembly assembly) {
        myAssemblies.add(assembly);
    }

    /**
     * Paint all shapes on the canvas.
     * else
     * 
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
     * 
     * @param dt used to increment time
     */
    public void update (double dt) {
        for (Assembly a : myAssemblies) {
            a.updateMovers(dt);
        }
    }

    /**
     * Returns size (in pixels) of the game area.
     */
    public Dimension getSize () {
        return myContainer.getSize();
    }

    /**
     * Gets Environment object through simulation.
     * 
     */
    public Environment getEnvironment () {
        return myEnvironment;
    }

    /**
     * Gets list of assemblies.
     * 
     */
    public List<Assembly> getMyAssemblies () {
        return myAssemblies;
    }

    /**
     * Clears the list of assemblies.
     */
    public void clearAssemblies () {
        myAssemblies.clear();
    }

    /**
     * Gets offset value for walled area.
     */
    public int getMyWalledAreaOffset () {
        return myWalledAreaOffset;
    }

    /**
     * Changes walled area offset.
     * 
     * @param offsetIncrement used for how much to increment
     */
    public void changeMyWalledAreaOffset (int offsetIncrement) {
        myWalledAreaOffset += offsetIncrement;
        System.out.println("Offset is now " + myWalledAreaOffset);
    }
}
