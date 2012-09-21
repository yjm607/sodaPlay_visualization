package mechanics;
import java.awt.geom.Point2D;


/**
 * This class represents a force applied in a specific direction and magnitude.
 * 
 * @author Robert c. Duvall
 */
public class Force {
    // angle in degrees
    private double myAngle;
    // "speed" in pixels per second
    private double myMagnitude;

    /**
     * Create a powerless force, i.e., with no magnitude.
     */
    public Force () {
        this(0, 0);
    }

    /**
     * Create a force in the given direction with the given magnitude.
     */
    public Force (double angle, double magnitude) {
        setDirection(angle);
        setMagnitude(magnitude);
    }

    /**
     * Create a force whose direction and magnitude are determined by the
     * direction and distance between the two given points.
     */
    public Force (Point2D source, Point2D target) {
        double dx = target.getX() - source.getX();
        double dy = source.getY() - target.getY();
        setDirection(angleBetween(dx, dy));
        setMagnitude(distanceBetween(dx, dy));
    }

    /**
     * Create a force that is identical to the given other force.
     */
    public Force (Force other) {
        this(other.getDirection(), other.getMagnitude());
    }

    /**
     * Reset this force to zero.
     */
    public void reset () {
        setDirection(0);
        setMagnitude(0);
    }

    /**
     * Returns this force's magnitude (in pixels).
     */
    public double getMagnitude () {
        return myMagnitude;
    }

    /**
     * Returns this force's magnitude relative to the given other force. More
     * formally, returns the magnitude of this force projected onto the given
     * other force.
     */
    public double getRelativeMagnitude (Force other) {
        return -getMagnitude()
                * Math.cos(Math.toRadians(getAngleBetween(other)));
    }

    /**
     * Scales this force's magnitude by the given change value.
     * <UL>
     * <LI>A value of 1 leaves the magnitude unchanged
     * <LI>Values less than 1 reduce the magnitude
     * <LI>Values greater than 1 increase the magnitude
     * </UL>
     */
    public void scale (double change) {
        setMagnitude(getMagnitude() * change);
    }

    /**
     * Sets this force's magnitude to the given value.
     */
    protected void setMagnitude (double value) {
        myMagnitude = value;
    }

    /**
     * Returns this force's direction (in degrees).
     */
    public double getDirection () {
        // standardize between -360 and +360 (keep 360, -360, and 0 as distinct
        // values)
        final double OFFSET = 0.001;
        double sign = (myAngle < 0) ? 1 : -1;
        return ((myAngle + sign * OFFSET) % 360) - sign * OFFSET;
    }

    /**
     * Returns the angle between this force and the given other force.
     */
    public double getAngleBetween (Force other) {
        return getDirection() - other.getDirection();
    }

    /**
     * Adjusts this force's direction by the given change value.
     */
    public void turn (double change) {
        setDirection(getDirection() + change);
    }

    /**
     * Sets this force's direction to the given value.
     */
    protected void setDirection (double value) {
        myAngle = value;
    }

    /**
     * Returns the change in the X direction represented by this force.
     */
    public double getXChange () {
        return getMagnitude() * Math.cos(Math.toRadians(getDirection()));
    }

    /**
     * Returns the change in the Y direction represented by this force.
     */
    public double getYChange () {
        return getMagnitude() * Math.sin(Math.toRadians(getDirection()));
    }

    /**
     * Returns a force that is the sum of this force and the given other force.
     */
    public void sum (Force other) {
        // double a1 = getAngle();
        // double a2 = other.getAngle();
        // double m1 = getMagnitude();
        // double m2 = other.getMagnitude();
        // double speed = Math.sqrt(m1 * m1 + m2 * m2 + 2 * m1 * m2 *
        // Math.cos(Math.toRadians(a1 - a2)));
        // double angle = Math.toDegrees(Math.asin(m2 *
        // Math.sin(Math.toRadians(a2 - a1)) / speed)) + a1;
        // return new Force(angle, speed);

        // more readable, although slightly slower
        double dx = getXChange() + other.getXChange();
        double dy = getYChange() + other.getYChange();
        setDirection(angleBetween(dx, dy));
        setMagnitude(distanceBetween(dx, dy));
    }

    /**
     * Returns a force that is the difference between this force and the given
     * other force.
     */
    public void difference (Force other) {
        other.negate();
        sum(other);
    }

    /**
     * Returns a force of the same magnitude, but in the opposite direction as
     * this force.
     */
    public void negate () {
        turn(180);
    }

    /**
     * Returns the average of this force with the given other force.
     */
    public Force average (Force other) {
        return new Force((getDirection() + other.getDirection()) / 2.0,
                (getMagnitude() + other.getMagnitude()) / 2.0);
    }

    /**
     * Return true if this force has the same magnitude and direction
     * as the given other force.
     */
    @Override
    public boolean equals (Object force) {
        final double EPSILON = 0.000001;
        try {
            Force other = (Force) force;
            return (Math.abs(getMagnitude() - other.getMagnitude()) < EPSILON && Math
                    .abs(getDirection() - other.getDirection()) < EPSILON);
        }
        catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Returns this force's values formatted as a string.
     */
    @Override
    public String toString () {
        return String.format("(%1.2f, %1.2f)", getDirection(), getMagnitude());
    }

    /**
     * Returns the distance between given two points
     */
    public static double distanceBetween (Point2D p1, Point2D p2) {
        return distanceBetween(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    /**
     * Returns the distance represented by the given dx and dy
     */
    public static double distanceBetween (double dx, double dy) {
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Returns the angle between the given two points
     */
    public static double angleBetween (Point2D p1, Point2D p2) {
        return angleBetween(p1.getY() - p2.getY(), p1.getX() - p2.getX());
    }

    /**
     * Returns the angle represented by the given dx and dy
     */
    public static double angleBetween (double dx, double dy) {
        return Math.toDegrees(Math.atan2(dy, dx));
    }
}
