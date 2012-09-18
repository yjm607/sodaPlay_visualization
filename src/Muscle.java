
/**
 * 
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
        // could extend input to vary frequency for each muscle
        myFrequency = 1.5;
    }

    @Override
    public void update (Simulation canvas, double dt) {
        myLength = myInitialLength + myAmplitude
                * Math.sin(myFrequency * myMuscleAge);
        forceLengthToNatural();
        myMuscleAge += dt;
    }
}
