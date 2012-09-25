package drawings;

import java.awt.Color;
import java.awt.Graphics2D;
import mechanics.Assembly;
import mechanics.Simulation;

/**
 * Creates a Drawable Bar object
 * @author Volodymyr Zavidovych & Jei Min Yoo
 *
 */
public class Bar extends Spring {

    /**
     * Construct a Bar object
     * @param start is a Mass on one end
     * @param end is a Mass on the other end
     * @param length is the length of the Bar
     * @param kVal indicates that the object is a Bar.
     */
    public Bar (Mass start, Mass end, double length, double kVal) {
        super(start, end, length, kVal);
        setLength(getDistanceBetweenEnds());
    }

    @Override
    public void update (Simulation canvas, Assembly assembly, double dt) {
        forceLengthToNatural();
    }

    @Override
    public void chooseLineStyle (Graphics2D pen) {
        pen.setColor(Color.BLACK);
    }

    @Override
    public String getClassName () {
        return "bar";
    }
}
