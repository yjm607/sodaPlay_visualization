package mechanics;

import drawings.Mass;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Handles gravity force.
 * 
 * @author Jei Min Yoo, Volodymyr Zavidovych
 *
 */
public class GravityForce extends Force {
    private static double ourGravityAngle = 0;
    private static double ourGravityMagnitude = 0;
    private int myKeyCode = KeyEvent.VK_G;
    /**
     * @param mass mass to apply force to
     * @param forces information about environment's forces
     */
    public GravityForce (Mass mass, HashMap<Integer, Force> forces) {
        boolean forceToggle = forces.get(myKeyCode).getToggle();
        setMagnitude(ourGravityMagnitude);
        setDirection(ourGravityAngle);
        setToggle(forceToggle);
        scale(mass.getMass());
    }

    /**
     * Processes line that came from factory
     * 
     * @param line line of input
     */
    public static void readInputLine(Scanner line) {
        ourGravityAngle = line.nextDouble();
        ourGravityMagnitude = line.nextDouble();
    }
}
