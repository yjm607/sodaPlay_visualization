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
    private int myKeyCode;

    /**
     * Wall Force constructor
     * 
     * @param mass mass to apply force to
     * @param forces information about environment's forces
     * @param container information about dimensions
     * @param keyCode keycode of the wall
     */
    public WallForce (Mass mass, HashMap<Integer, Force> forces, int keyCode, Canvas container) {
        double distance = 0;
        double angle = 0;
        myKeyCode = keyCode;
        switch (keyCode) {
            case KeyEvent.VK_1:
                angle = Canvas.DOWN_ANGLE;
                distance = mass.getCenter().getY() / Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            case KeyEvent.VK_2:
                angle = Canvas.LEFT_ANGLE;
                distance =
                        (container.getSize().width - mass.getCenter().getX()) /
                                Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            case KeyEvent.VK_3:
                angle = Canvas.UP_ANGLE;
                distance =
                        (container.getSize().height - mass.getCenter().getY()) /
                                Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            case KeyEvent.VK_4:
                angle = Canvas.RIGHT_ANGLE;
                distance = mass.getCenter().getY() / Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            default:
                break;
        }
        boolean currentToggle = forces.get(keyCode).getToggle();
        Force unitForce = new Force();
        double exponent = 0;
        for (Map.Entry<Force, Double> entry : ourRawWallForces.entrySet()) {
            if (entry.getKey().getDirection() == angle) {
                exponent = entry.getValue();
                unitForce = new Force(entry.getKey());
            }
        }
        unitForce.scale(1 / Math.pow(distance, exponent));
        setToggle(currentToggle);
        setMagnitude(unitForce.getMagnitude());
        setDirection(unitForce.getDirection());
    }

    /**
     * @return returns wall's keyCode
     */
    public int getKeyCode () {
        return myKeyCode;
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
