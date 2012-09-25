package mechanics;

import drawings.Drawable;
import drawings.Mass;

/**
 * Handles center mass force.
 * 
 * @author Jei Min Yoo, Volodymyr Zavidovych
 *
 */
public class CenterMassForce extends Force {
    /**
     * @param mass mass to apply force to
     * @param assembly assembly of the mass
     * @param centerMassMagnitude magnitude of the center mass force
     * @param centerMassExponent exponent of the center mass force
     * @param forceToggle whether or not the force is on
     */
    public CenterMassForce (Mass mass, Assembly assembly, double centerMassMagnitude,
                            double centerMassExponent, boolean forceToggle) {
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
        double magnitude = centerMassMagnitude / Math.pow(distance, centerMassExponent);
        setDirection(angle);
        setMagnitude(magnitude);
        setToggle(forceToggle);
    }
}
