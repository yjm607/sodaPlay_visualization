package mechanics;

import drawings.Mass;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Handles viscosity force.
 * 
 * @author Jei Min Yoo, Volodymyr Zavidovych
 *
 */
public class ViscosityForce extends Force {

    private static double ourViscosity = 0;
    private int myKeyCode = KeyEvent.VK_V;
    /**
     * @param mass mass to apply force to
     * @param forces information about environment's forces
     */
    public ViscosityForce (Mass mass, HashMap<Integer, Force> forces) {
        boolean forceToggle = forces.get(myKeyCode).getToggle();
        Force velocity = new Force(mass.getVelocity());
        velocity.negate();
        setMagnitude(velocity.getMagnitude() * ourViscosity);
        setDirection(velocity.getDirection());
        setToggle(forceToggle);
    }

    /**
     * Processes line that came from factory
     * 
     * @param line line of input
     */
    public static void readInputLine(Scanner line) {
        ourViscosity = line.nextDouble();
    }
}
