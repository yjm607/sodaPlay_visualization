package drawings;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
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

    public Spring (Mass start, Mass end, double length, double kVal) {
        myStart = start;
        myEnd = end;
        myLength = length;
        myK = kVal;
        myLength = length;
    }

    public void paint (Graphics2D pen) {
        int xStart = (int) myStart.getCenter().getX();
        int yStart = (int) myStart.getCenter().getY();
        int xEnd = (int) myEnd.getCenter().getX();
        int yEnd = (int) myEnd.getCenter().getY();
        chooseLineStyle(pen);
        pen.drawLine(xStart, yStart, xEnd, yEnd);
    }

    public void update (Simulation canvas, double dt) {
        // apply hooke's law to each attached mass
        double dx = getXLengthComponent();
        double dy = getYLengthComponent();
        Force f = new Force(Force.angleBetween(dx, dy), myK
                * (myLength - Force.distanceBetween(dx, dy)));
        myStart.applyForce(f);
        f.negate();
        myEnd.applyForce(f);
    }

    public void forceLengthToNatural () {
        double dx = getXLengthComponent();
        double dy = getYLengthComponent();
        double myAngle = Math.toRadians(Force.angleBetween(dx, dy));
        double myDistanceBetweenPoints = Force.distanceBetween(dx, dy);
        double myLengthIncrement = (myLength - myDistanceBetweenPoints) / 2;
        if (!myStart.isFixed()) {
            myStart.shiftCenter(myLengthIncrement, myAngle);
        }
        if (!myEnd.isFixed()) {
            myEnd.shiftCenter(-myLengthIncrement, myAngle);
        }
    }

    public double getXLengthComponent() {
        return myStart.getCenter().getX() - myEnd.getCenter().getX();
    }

    public double getYLengthComponent() {
        return myStart.getCenter().getY() - myEnd.getCenter().getY();
    }

    public void setLength(double newLength) {
        myLength = newLength;
    }

    public double getDistanceBetweenEnds () {
        return Math.sqrt(getXLengthComponent() * getXLengthComponent()
                + getYLengthComponent() * getYLengthComponent());
    }

    public void chooseLineStyle(Graphics2D pen) {
        double len = getDistanceBetweenEnds() - myLength;
        if (len < 0.0) {
            pen.setColor(Color.BLUE);
        }
        else {
            pen.setColor(Color.RED);
        }
    }
}
