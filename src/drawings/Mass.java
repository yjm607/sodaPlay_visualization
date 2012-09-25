package drawings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import mechanics.Assembly;
import mechanics.Canvas;
import mechanics.Force;
import mechanics.Simulation;

/**
 * @author Robert C. Duvall, edited by Jei Yoo & Volodymyr Zavidovych
 */
public class Mass implements Drawable {
    // state
    private Point2D myCenter;
    private Force myVelocity;
    private Dimension mySize;
    private final int mySizeConstant = 16;
    private int myID;
    private double myMass;
    private Force myAcceleration;
    private boolean myMassIsFixed;
    private final double myScaleConstant = -2.0;

    /**
     * @param id is ID of the Mass
     * @param x is x position of Mass
     * @param y is Y position of Mass
     * @param mass is the mass(weight) value of the Mass
     */
    public Mass (int id, double x, double y, double mass) {
        myAcceleration = new Force();
        myMass = mass;
        myID = id;
        setCenter(x, y);
        setVelocity(0, 0);
        mySize = new Dimension(mySizeConstant, mySizeConstant);
        myMassIsFixed = myMass <= 0;
    }

    @Override
    public void paint (Graphics2D pen) {
        pen.setColor(Color.BLACK);
        pen.fillOval(getLeft(), getTop(), getSize().width, getSize().height);
    }

    @Override
    public void update (Simulation canvas, Assembly assembly, double dt) {
        applyForce(canvas.getEnvironment().getAllForces(this, assembly));
        // convert force back into Mover's velocity
        myAcceleration.scale(1.0 / myMass);
        getVelocity().sum(myAcceleration);
        myAcceleration.reset();
        getBounce(canvas);
        // move mass by velocity if mass isn't fixed
        if (!myMassIsFixed) {
            myCenter.setLocation(myCenter.getX() + myVelocity.getXChange() * dt,
                                 myCenter.getY() + myVelocity.getYChange() * dt);
        }
    }

    /**
     * @param f
     *        force to be applied to this mass
     */
    public void applyForce (Force f) {
        myAcceleration.sum(f);
    }

    @Override
    public String getClassName () {
        return "mass";
    }

    @Override
    public boolean match (int id) {
        return myID == id;
    }

    // check for move out of bounds
    private void getBounce (Simulation canvas) {
        Dimension bounds = canvas.getSize();
        int walledAreaOffset = canvas.getMyWalledAreaOffset();
        applyBounceForce(bounds, walledAreaOffset);
    }

    private void applyBounceForce (Dimension bounds, int walledAreaOffset) {
        Force normal = new Force();
        if (getLeft() < -walledAreaOffset) {
            normal = new Force(Canvas.RIGHT_ANGLE, 1);
            setCenter((getSize().width / 2) - walledAreaOffset, getCenter().getY());
        }
        else if (getRight() > bounds.width + walledAreaOffset) {
            normal = new Force(Canvas.LEFT_ANGLE, 1);
            setCenter(bounds.width - getSize().width / 2 + walledAreaOffset, getCenter().getY());
        }
        if (getTop() < -walledAreaOffset) {
            normal = new Force(Canvas.DOWN_ANGLE, 1);
            setCenter(getCenter().getX(), (getSize().height / 2) - walledAreaOffset);
        }
        else if (getBottom() > bounds.height + walledAreaOffset) {
            normal = new Force(Canvas.UP_ANGLE, 1);
            setCenter(getCenter().getX(), bounds.height - getSize().height / 2 + walledAreaOffset);
        }
        normal.scale(myScaleConstant * normal.getRelativeMagnitude(myVelocity) *
                     myVelocity.getMagnitude());
        myVelocity.sum(normal);
    }

    /**
     * Returns shape's velocity.
     */
    public Force getVelocity () {
        return myVelocity;
    }

    /**
     * Resets shape's velocity.
     * 
     * @param direction sets velocity direction
     * @param magnitude sets velocity magnitude
     */
    public void setVelocity (double direction, double magnitude) {
        myVelocity = new Force(direction, magnitude);
    }

    /**
     * Returns shape's center.
     */
    public Point2D getCenter () {
        return myCenter;
    }

    /**
     * Resets shape's center.
     * 
     * @param x sets x coordinate of the mass.
     * @param y sets y coordinate of the mass.
     */
    public void setCenter (double x, double y) {
        myCenter = new Point2D.Double(x, y);
    }

    /**
     * Shift shape's center by increment in the specified direction.
     * 
     * @param increment determines how much to change the location in one step.
     * @param angle determines the angle of the change.
     */
    public void shiftCenter (double increment, double angle) {
        setCenter(getCenter().getX() + increment * Math.cos(Math.toRadians(angle)),
                  getCenter().getY() + increment * Math.sin(Math.toRadians(angle)));
    }

    /**
     * Get the mass value.
     */
    public double getMass () {
        return myMass;
    }

    /**
     * Returns shape's left-most coordinate.
     */
    public int getLeft () {
        return (int) (getCenter().getX() - getSize().width / 2);
    }

    /**
     * Returns shape's top-most coordinate.
     */
    public int getTop () {
        return (int) (getCenter().getY() - getSize().height / 2);
    }

    /**
     * Returns shape's right-most coordinate.
     */
    public int getRight () {
        return (int) (getCenter().getX() + getSize().width / 2);
    }

    /**
     * Reports shape's bottom-most coordinate.
     * 
     * @return bottom-most coordinate
     */
    public int getBottom () {
        return (int) (getCenter().getY() + getSize().height / 2);
    }

    /**
     * Reports fixed-ess state.
     */
    public boolean isFixed () {
        return myMassIsFixed;
    }

    /**
     * Returns shape's size.
     */
    public Dimension getSize () {
        return mySize;
    }
}
