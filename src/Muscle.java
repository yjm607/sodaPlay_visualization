import java.awt.Color;
import java.awt.Graphics2D;


/**
 * @author Jei Yoo & Volodymyr Zavidovych
 */
public class Muscle extends Spring {

    private double myAmplitude;
    private double myInitialLength;
    private double myMuscleAge;
    private double myFrequency;

    public Muscle (Mass start, Mass end, double length,
            double kVal, double amplitude) {
        super(start, end, length, kVal);
        myAmplitude = amplitude;
        myInitialLength = length;
        myMuscleAge = 0;
        myFrequency = Canvas.MUSCLE_OSCILLATION_PERIOD;
    }

    @Override
    public void update (Simulation canvas, double dt) {
        updateMuscleNaturalLength();
        forceLengthToNatural();
        myMuscleAge += dt;
    }

    public void updateMuscleNaturalLength() {
        double newNaturalLength = myInitialLength + myAmplitude
                * Math.sin(myFrequency * myMuscleAge);
        setLength(newNaturalLength);
    }
    
    @Override
    public void chooseLineStyle(Graphics2D pen) {
        double len = getDistanceBetweenEnds() - myInitialLength;
        if (len < 0.0) {
            pen.setColor(Color.BLUE);
        }
        else {
            pen.setColor(Color.RED);
        }
    }
}
