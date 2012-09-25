package drawings;

import java.awt.Color;
import java.awt.Graphics2D;
import mechanics.Assembly;
import mechanics.Canvas;
import mechanics.Simulation;


/**
 * @author Jei Yoo & Volodymyr Zavidovych
 */
public class Muscle extends Spring {

    private double myAmplitude;
    private double myInitialLength;
    private double myMuscleAge;
    private double myFrequency;

    /**
     * Construct a muscle  object
     * @param start is a Mass on one end
     * @param end is a Mass on the other end
     * @param length is the length of the muscle
     * @param kVal determines the k value of the muscle.
     * @param amplitude determines the amplitude of muscle movement.
     */
    public Muscle (Mass start, Mass end, double length, double kVal,
            double amplitude) {
        super(start, end, length, kVal);
        myAmplitude = amplitude;
        myInitialLength = length;
        myMuscleAge = 0;
        myFrequency = Canvas.MUSCLE_OSCILLATION_PERIOD;
    }

    @Override
    public void update (Simulation canvas, Assembly assembly, double dt) {
        updateMuscleNaturalLength();
        forceLengthToNatural();
        myMuscleAge += dt;
    }

    /**
     * changes muscle's natural strength as it extends and compresses.
     */
    public void updateMuscleNaturalLength () {
        double newNaturalLength = myInitialLength + myAmplitude *
                Math.sin(myFrequency * myMuscleAge);
        setLength(newNaturalLength);
    }

    @Override
    public String getClassName () {
        return "muscle";
    }

    @Override
    public void chooseLineStyle (Graphics2D pen) {
        double len = getDistanceBetweenEnds() - myInitialLength;
        if (len < 0.0) {
            pen.setColor(Color.BLUE);
        }
        else {
            pen.setColor(Color.RED);
        }
    }
}
