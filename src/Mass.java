import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;


/**
 * 
 * @author Robert C. Duvall, edited by Jei Yoo & Volodymyr Zavidovych
 */
public class Mass {
    // state
    private Point2D myCenter;
    private Force myVelocity;
    private Dimension mySize;
    private int myID;
    private double myMass;
    private Force myAcceleration;
    private boolean isFixed;

    /**
     * @param id
     * @param x
     * @param y
     * @param mass
     */
    public Mass (int id, double x, double y, double mass) {
        myAcceleration = new Force();
        myMass = mass;
        myID = id;
        setCenter(x, y);
        setVelocity(0, 0);
        mySize = new Dimension(16, 16);
        isFixed = myMass <= 0;
    }

    public void paint (Graphics2D pen) {
        pen.setColor(Color.BLACK);
        pen.fillOval(getLeft(), getTop(), getSize().width, getSize().height);
    }

    public void update (Simulation canvas, double dt) {
        applyForce(getGravity());
        applyForce(getBounce(canvas.getSize()));
        // convert force back into Mover's velocity
        getVelocity().sum(myAcceleration);
        myAcceleration.reset();
        // move mass by velocity if mass isn't fixed
        if (!isFixed) {
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

    public boolean match (int id) {
        return myID == id;
    }

    // add gravity towards bottom
    private Force getGravity () {
        Force result = new Force(90, 1);
        result.scale(1.0 / myMass);
        return result;
    }

    // check for move out of bounds
    private Force getBounce (Dimension bounds) {
        Force impulse = new Force();
        if (getLeft() < 0) {
            impulse = new Force(0, 2);
            setCenter(getSize().width / 2, getCenter().getY());
        }
        else if (getRight() > bounds.width) {
            impulse = new Force(180, 2);
            setCenter(bounds.width - getSize().width / 2, getCenter().getY());
        }
        if (getTop() < 0) {
            impulse = new Force(270, 2);
            setCenter(getCenter().getX(), getSize().height / 2);
        }
        else if (getBottom() > bounds.height) {
            impulse = new Force(90, 2);
            setCenter(getCenter().getX(), bounds.height - getSize().height / 2);
        }
        impulse.scale(getVelocity().getRelativeMagnitude(impulse));
        return impulse;
    }

    /**
     * Returns shape's velocity.
     */
    public Force getVelocity () {
        return myVelocity;
    }

    /**
     * Resets shape's velocity.
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
     */
    public void setCenter (double x, double y) {
        myCenter = new Point2D.Double(x, y);
    }
    
    /**
     * Shift shape's center by increment in the specified direction.
     */
    public void shiftCenter (double increment, double angle) {
        setCenter(
                getCenter().getX() + increment * Math.cos(angle),
                getCenter().getY() + increment * Math.sin(angle));
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
        return isFixed;
    }

    /**
     * Returns shape's size.
     */
    public Dimension getSize () {
        return mySize;
    }
}
