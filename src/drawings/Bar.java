package drawings;

import java.awt.Color;
import java.awt.Graphics2D;
import mechanics.Assembly;
import mechanics.Simulation;


public class Bar extends Spring {

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
