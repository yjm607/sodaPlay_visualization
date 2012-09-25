package drawings;

import java.awt.Graphics2D;
import mechanics.Assembly;
import mechanics.Simulation;


public interface Drawable {

    boolean match (int id);

    void paint (Graphics2D pen);

    void update (Simulation canvas, Assembly assembly, double dt);

    String getClassName ();
}
