package mechanics;

import drawings.Mass;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles wall force.
 * 
 * @author Jei Min Yoo, Volodymyr Zavidovych
 *
 */
public class WallForce extends Force {

    private static HashMap<Force, Double> ourRawWallForces = new HashMap<Force, Double>();
    private double myExponent;
    private double myDistance;
    private double myAngle;

    /**
     * Wall Force constructor
     * 
     * @param mass mass to apply force to
     * @param forces information about environment's forces
     * @param container information about dimensions
     * @param keyCode keycode of the wall
     */
    public WallForce (Mass mass, HashMap<Integer, Force> forces, int keyCode, Canvas container) {
        initialize();
        calculateDistanceToMassAndAngle(mass, keyCode, container);
        setDirection(myAngle);
        setMagnitude(myDistance, myAngle);
        boolean currentToggle = forces.get(keyCode).getToggle();
        setToggle(currentToggle);
    }

    private void calculateDistanceToMassAndAngle (Mass mass, int keyCode, Canvas container) {
        switch (keyCode) {
            case KeyEvent.VK_1:
                myAngle = Canvas.DOWN_ANGLE;
                myDistance = mass.getCenter().getY() / Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            case KeyEvent.VK_2:
                myAngle = Canvas.LEFT_ANGLE;
                myDistance =
                        (container.getSize().width - mass.getCenter().getX()) /
                                Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            case KeyEvent.VK_3:
                myAngle = Canvas.UP_ANGLE;
                myDistance =
                        (container.getSize().height - mass.getCenter().getY()) /
                                Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            case KeyEvent.VK_4:
                myAngle = Canvas.RIGHT_ANGLE;
                myDistance = mass.getCenter().getY() / Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            default:
                break;
        }
    }

    private void initialize () {
        myExponent = 0;
        myDistance = 0;
        myAngle = 0;
    }

    private void setMagnitude (double distance, double angle) {
        for (Map.Entry<Force, Double> entry : ourRawWallForces.entrySet()) {
            if (entry.getKey().getDirection() == angle) {
                myExponent = entry.getValue();
                super.setMagnitude(entry.getKey().getMagnitude());
            }
        }
        scale(1 / Math.pow(distance, myExponent));
    }

    /**
     * Processes line that came from factory
     * 
     * @param line line of input
     */
    public static void readInputLine(Scanner line) {
        int id = line.nextInt();
        double magnitude = line.nextDouble();
        double exponent = line.nextDouble();
        Force force = new Force();
        switch (id) {
            case 1:
                force = new Force(Canvas.DOWN_ANGLE, magnitude);
                break;
            case 2:
                force = new Force(Canvas.LEFT_ANGLE, magnitude);
                break;
            case 3:
                force = new Force(Canvas.UP_ANGLE, magnitude);
                break;
            case 4:
                force = new Force(Canvas.RIGHT_ANGLE, magnitude);
                break;
            default:
                break;
        }
        ourRawWallForces.put(force, exponent);
    }
}
