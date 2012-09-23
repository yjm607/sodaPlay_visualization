package mechanics;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import drawings.Drawable;
import drawings.Mass;

/**
 * Simulates objects moving around in a bounded environment.
 * 
 * @author Robert C. Duvall
 */
public class Simulation {
    private List<Drawable> myDrawings;
    private Canvas myContainer;
    private Force myGravity;
    private double myViscosity;
    private double myCenterMassForce;
    private double myCenterMassExponent;


    /**
     * Create a Canvas with the given size.
     */
    public Simulation (Canvas container) {
        myDrawings = new ArrayList<Drawable>();
        myContainer = container;
    }

    public void add (Drawable drawing) {
        myDrawings.add(drawing);
    }
    
    public void add (Force gravity) {
        myGravity = gravity;
    }
    
    public void add (double viscosity) {
        myViscosity = viscosity;
    }
    
    public void add (double [] data, String type ) {
        if (type.equals("centermass")) {
            myCenterMassForce = data[0];
            myCenterMassExponent = data[1];
        }
    }
    
    /**
     * Paint all shapes on the canvas.
     * else 
     * @param pen used to paint shape on the screen
     */
    public void paint (Graphics2D pen) {
        for (Drawable d : myDrawings) {
            d.paint(pen);
        }
    }

    /**
     * Called by each step of timer, multiple times per second.
     * 
     * This should update the state of the animated shapes by just
     * a little so they appear to move over time.
     */
    public void update (double dt) {
        updateMovers(dt);
    }

    void updateMovers (double dt) {
        for (Drawable d : myDrawings) {
            d.update(this, dt);
        }
    }

    /**
     * Returns size (in pixels) of the game area.
     */
    public Dimension getSize () {
        return myContainer.getSize();
    }

    public Drawable getDrawable (int id) {
        for (Drawable d : myDrawings) {
            if (d.match(id)) return d;
        }
        return null;
    }

    public Force getGravity(double mass) {
        Force result = new Force(myGravity);
        result.scale(mass);
        return result;
    }
    
    public Force getViscosity(Mass mass) {
        Force velocity = new Force(mass.getVelocity());
        velocity.negate();
        Force result = new Force(velocity.getDirection(), velocity.getMagnitude() * myViscosity);
        return result;
    }
    
    public Force getCenterMass(Mass mass) {
        double xCenter = 0;
        double yCenter = 0;
        double totalMass = 0;
        for (Drawable d : myDrawings) {
            if(d.getClass().toString().equals("class drawings.Mass")) {
                xCenter += ((Mass) d).getMass() * ((Mass) d).getCenter().getX();
                yCenter += ((Mass) d).getMass() * ((Mass) d).getCenter().getY();
                totalMass += ((Mass) d).getMass();
            }
        }
        double dx = xCenter / totalMass - mass.getCenter().getX();
        double dy = yCenter / totalMass - mass.getCenter().getY();
        double angle = Force.angleBetween(dx, dy);
        double distance = Force.distanceBetween(dx, dy)
                / Canvas.CENTER_MASS_FORCE_DISTANCE_DIVIDER ;
        double magnitude = myCenterMassForce / Math.pow(distance,myCenterMassExponent);
        return new Force(angle, magnitude);
    }
}
