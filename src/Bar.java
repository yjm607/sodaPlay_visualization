import java.awt.Color;
import java.awt.Graphics2D;


public class Bar extends Spring {

    public Bar (Mass start, Mass end, double length, double kVal) {
        super(start, end, length, kVal);
        setLength(getDistanceBetweenEnds());
    }
    
    @Override
    public void update(Simulation canvas, double dt) {
        forceLengthToNatural();
    }
    
    @Override
    public void chooseLineStyle(Graphics2D pen) {
        pen.setColor(Color.BLACK);
    }
}
