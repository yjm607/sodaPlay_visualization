package mechanics;

import drawings.Mass;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * Handles wall force.
 * 
 * @author Jei Min Yoo, Volodymyr Zavidovych
 *
 */
public class WallForce extends Force {
    private int myKeyCode;

    /**
     * Wall Force constructor
     * 
     * @param mass mass to apply force to
     * @param unitForce unit wall force
     * @param exponent wall force exponent
     * @param forces information about environment's forces
     * @param container information about dimensions
     */
    public WallForce (Mass mass, Force unitForce, double exponent, HashMap<Integer, Force> forces,
                      Canvas container) {
        double distance = 0;
        switch ((int) unitForce.getDirection()) {
            case Canvas.DOWN_ANGLE:
                myKeyCode = KeyEvent.VK_1;
                distance = mass.getCenter().getY() / Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            case Canvas.LEFT_ANGLE:
                myKeyCode = KeyEvent.VK_2;
                distance =
                        (container.getSize().width - mass.getCenter().getX()) /
                                Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            case Canvas.UP_ANGLE:
                myKeyCode = KeyEvent.VK_3;
                distance =
                        (container.getSize().height - mass.getCenter().getY()) /
                                Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            case Canvas.RIGHT_ANGLE: 
                myKeyCode = KeyEvent.VK_4;
                distance = mass.getCenter().getY() / Canvas.FORCE_DISTANCE_DIVIDER;
                break;
            default:
                break;
        }
        boolean currentToggle = forces.get(myKeyCode).getToggle();
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
}
