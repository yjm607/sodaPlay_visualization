package mechanics;

import drawings.Mass;


/**
 * Handles gravity force.
 * 
 * @author Jei Min Yoo, Volodymyr Zavidovych
 *
 */
public class GravityForce extends Force {
    /**
     * @param mass mass to apply force to
     * @param gravityMagnitude magnitude of the gravity force
     * @param gravityAngle angle of the gravity force
     * @param forceToggle whether or not the force is on
     */
    public GravityForce (Mass mass, double gravityAngle, double gravityMagnitude,
                         boolean forceToggle) {
        setMagnitude(gravityMagnitude);
        setDirection(gravityAngle);
        setToggle(forceToggle);
        scale(mass.getMass());
    }
}
