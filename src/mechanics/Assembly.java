package mechanics;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import drawings.Drawable;
import drawings.Mass;


public class Assembly {
    private List<Drawable> myDrawings;
    private Simulation mySimulation;

    public Assembly (Simulation sim) {
        myDrawings = new ArrayList<Drawable>();
        mySimulation = sim;
    }

    public void add (Drawable drawing) {
        myDrawings.add(drawing);
    }

    public void paint (Graphics2D pen) {
        for (Drawable d : myDrawings) {
            d.paint(pen);
        }
    }

    public void updateMovers (double dt) {
        for (Drawable d : myDrawings) {
            d.update(mySimulation, this, dt);
        }
    }

    public Drawable getDrawable (int id) {
        for (Drawable d : myDrawings) {
            if (d.match(id)) return d;
        }
        return null;
    }

    public List<Drawable> getMyDrawings () {
        return myDrawings;
    }

    public Mass getNearestMass (Point point) {
        Mass nearestMass = null;
        double minDistance = Math.max(mySimulation.getSize().getHeight(),
                mySimulation.getSize().getWidth());
        for (Drawable d : myDrawings) {
            if (d.getClassName().equals("mass")) {
                double distance = Force.distanceBetween(point,
                        ((Mass) d).getCenter());
                if (distance < minDistance) {
                    nearestMass = (Mass) d;
                    minDistance = distance;
                }
            }
        }
        return nearestMass;
    }

}
