package mechanics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import drawings.Bar;
import drawings.Mass;
import drawings.Muscle;
import drawings.Spring;


/**
 * Reads objects from files and puts them into object lists
 * 
 * @author Robert C. Duvall, edited by Jei Yoo & Volodymyr Zavidovych
 */
public class Factory {
    public void loadModel (Simulation sim, File modelFile) {
        try {
            Scanner input = new Scanner(modelFile);
            while (input.hasNext()) {
                Scanner line = new Scanner(input.nextLine());
                if (line.hasNext()) {
                    String type = line.next();
                    if (type.equals("mass")) {
                        sim.add(massCommand(line));
                    }
                    else if (type.equals("spring")) {
                        sim.add(springCommand(line, sim));
                    }
                    else if (type.equals("muscle")) {
                        sim.add(muscleCommand(line, sim));
                    }
                }
            }
            input.close();
        }
        catch (FileNotFoundException e) {
            // should not happen because File came from user selection
            e.printStackTrace();
        }
    }

    private Mass massCommand (Scanner line) {
        int id = line.nextInt();
        double x = line.nextDouble();
        double y = line.nextDouble();
        double mass = line.nextDouble();
        return new Mass(id, x, y, mass);
    }

    private Spring springCommand (Scanner line, Simulation sim) {
        int m1 = line.nextInt();
        int m2 = line.nextInt();
        double restLength = line.nextDouble();
        double ks = line.nextDouble();
        if (ks >= 0) {
            return new Spring((Mass) sim.getDrawable(m1), (Mass) sim.getDrawable(m2), restLength, ks);
        }
        else {
            return new Bar((Mass) sim.getDrawable(m1), (Mass) sim.getDrawable(m2), restLength, ks);
        }
        
    }

    private Spring muscleCommand (Scanner line, Simulation sim) {
        int m1 = line.nextInt();
        int m2 = line.nextInt();
        double restLength = line.nextDouble();
        double ks = line.nextDouble();
        double amplitude = line.nextDouble();
        return new Muscle((Mass) sim.getDrawable(m1), (Mass) sim.getDrawable(m2), restLength, ks,
                amplitude);
    }
}
