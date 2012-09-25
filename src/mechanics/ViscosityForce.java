package mechanics;

import drawings.Mass;

/**
 * Handles viscosity force.
 * 
 * @author Jei Min Yoo, Volodymyr Zavidovych
 *
 */
public class ViscosityForce extends Force {
    /**
     * @param mass mass to apply force to
     * @param viscosity viscosity
     * @param forceToggle whether or not the force is on
     */
    public ViscosityForce (Mass mass, double viscosity, boolean forceToggle) {
        Force velocity = new Force(mass.getVelocity());
        velocity.negate();
        setMagnitude(velocity.getMagnitude() * viscosity);
        setDirection(velocity.getDirection());
        setToggle(forceToggle);
    }
}
