package drawings;

import java.awt.Color;
import java.awt.Graphics2D;
import mechanics.Assembly;
import mechanics.Force;
import mechanics.Simulation;


/**
 * 
 * @author Robert C. Duvall, edited by Jei Yoo & Volodymyr Zavidovych
 */
public class Spring implements Drawable {
    private Mass myStart;
    private Mass myEnd;
    private double myLength;
    private double myK;

    /**
     * Construct a spring object
     * 
     * @param start is a Mass on one end
     * @param end is a Mass on the other end
     * @param length is the length of the spring
     * @param kVal determines K value of the spring
     */
    public Spring (Mass start, Mass end, double length, double kVal) {
        myStart = start;
        myEnd = end;
        myLength = length;
        myK = kVal;
        myLength = length;
    }

    @Override
    public void paint (Graphics2D pen) {
        int xStart = (int) myStart.getCenter().getX();
        int yStart = (int) myStart.getCenter().getY();
        int xEnd = (int) myEnd.getCenter().getX();
        int yEnd = (int) myEnd.getCenter().getY();
        chooseLineStyle(pen);
        pen.drawLine(xStart, yStart, xEnd, yEnd);
    }

    @Override
    public void update (Simulation canvas, Assembly assembly, double dt) {
        // apply hooke's law to each attached mass
        double dx = getXLengthComponent();
        double dy = getYLengthComponent();
        Force f =
                new Force(Force.angleBetween(dx, dy), myK *
                                                      (myLength - Force.distanceBetween(dx, dy)));
        myStart.applyForce(f);
        f.negate();
        myEnd.applyForce(f);
    }

    @Override
    public boolean match (int id) {
        return false;
    }

    @Override
    public String getClassName () {
        return "string";
    }

    /**
     * forces spring's length to its natural length
     */
    public void forceLengthToNatural () {
        double dx = getXLengthComponent();
        double dy = getYLengthComponent();
        double myAngle = Force.angleBetween(dx, dy);
        double myDistanceBetweenPoints = Force.distanceBetween(dx, dy);
        double myLengthIncrement = (myLength - myDistanceBetweenPoints) / 2;
        if (!myStart.isFixed()) {
            myStart.shiftCenter(myLengthIncrement, myAngle);
        }
        if (!myEnd.isFixed()) {
            myEnd.shiftCenter(-myLengthIncrement, myAngle);
        }
    }

    /**
     * gets spring's horizontal length
     */
    public double getXLengthComponent () {
        return myStart.getCenter().getX() - myEnd.getCenter().getX();
    }

    /**
     * gets spring's vertical length
     */
    public double getYLengthComponent () {
        return myStart.getCenter().getY() - myEnd.getCenter().getY();
    }

    /**
     * sets spring's natural length
     * 
     * @param newLength is the new natural length
     */
    public void setLength (double newLength) {
        myLength = newLength;
    }

    /**
     * gets spring's current length
     */
    public double getDistanceBetweenEnds () {
        return Math.sqrt(getXLengthComponent() * getXLengthComponent() + getYLengthComponent() *
                         getYLengthComponent());
    }

    /**
     * changes spring's color as it changes length
     * 
     * @param pen used to draw spring
     */
    public void chooseLineStyle (Graphics2D pen) {
        double len = getDistanceBetweenEnds() - myLength;
        if (len < 0.0) {
            pen.setColor(Color.BLUE);
        }
        else {
            pen.setColor(Color.RED);
        }
    }
}
