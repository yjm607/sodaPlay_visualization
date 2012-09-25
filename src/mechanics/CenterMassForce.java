package mechanics;

import drawings.Drawable;
import drawings.Mass;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Handles center mass force.
 * 
 * @author Jei Min Yoo, Volodymyr Zavidovych
 *
 */
public class CenterMassForce extends Force {

    private static double ourCenterMassMagnitude = 0;
    private static double ourCenterMassExponent = 0;
    private int myKeyCode = KeyEvent.VK_M;
    /**
     * @param mass mass to apply force to
     * @param assembly assembly of the mass
     * @param forceToggle whether or not the force is on
     */
    public CenterMassForce (Mass mass, Assembly assembly, HashMap<Integer,Force> forces) {
        boolean forceToggle = forces.get(myKeyCode).getToggle();
        double xCenter = 0;
        double yCenter = 0;
        double totalMass = 0;
        for (Drawable d : assembly.getMyDrawings()) {
            if ("mass".equals(d.getClassName())) {
                xCenter += ((Mass) d).getMass() * ((Mass) d).getCenter().getX();
                yCenter += ((Mass) d).getMass() * ((Mass) d).getCenter().getY();
                totalMass += ((Mass) d).getMass();
            }
        }
        double dx = xCenter / totalMass - mass.getCenter().getX();
        double dy = yCenter / totalMass - mass.getCenter().getY();
        double angle = Force.angleBetween(dx, dy);
        double distance = Force.distanceBetween(dx, dy) / Canvas.FORCE_DISTANCE_DIVIDER;
        double magnitude = ourCenterMassMagnitude / Math.pow(distance, ourCenterMassExponent);
        setDirection(angle);
        setMagnitude(magnitude);
        setToggle(forceToggle);
    }

    /**
     * Processes line that came from factory
     * 
     * @param line line of input
     */
    public static void readInputLine(Scanner line) {
        ourCenterMassMagnitude = line.nextDouble();
        ourCenterMassExponent = line.nextDouble();
    }
}
