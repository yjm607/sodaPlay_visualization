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
            if (modelFile.getName().equals("environment.xsp")) {
                loadEnvironment(sim, input);
            }
            else {
                loadAssembly(sim, input);
            }
//            while (input.hasNext()) {
//                Scanner line = new Scanner(input.nextLine());
//                if (line.hasNext()) {
//                    String type = line.next();
//                    if (type.equals("mass")) {
//                        sim.add(massCommand(line));
//                    }
//                    else if (type.equals("spring")) {
//                        sim.add(springCommand(line, sim));
//                    }
//                    else if (type.equals("muscle")) {
//                        sim.add(muscleCommand(line, sim));
//                    }
//                    else if (type.equals("gravity")) {
//                        sim.add(line, type);
//                    }
//                    else if (type.equals("viscosity")) {
//                        sim.add(line, type);
//                    }
//                    else if (type.equals("centermass")) {
//                        sim.add(line, type);
//                    }
//                    else if (type.equals("wall")) {
//                        sim.add(line, type);
//                    }
//                }
//            }
            input.close();
        }
        catch (FileNotFoundException e) {
            // should not happen because File came from user selection
            e.printStackTrace();
        }
    }

    private void loadAssembly (Simulation sim, Scanner input) {
        Assembly assembly = new Assembly(sim);
        while (input.hasNext()) {
            Scanner line = new Scanner(input.nextLine());
            if (line.hasNext()) {
                String type = line.next();
                if (type.equals("mass")) {
                    assembly.add(massCommand(line));
                }
                else if (type.equals("spring")) {
                    assembly.add(springCommand(line, assembly));
                }
                else if (type.equals("muscle")) {
                    assembly.add(muscleCommand(line, assembly));
                }
            }
        }
        sim.add(assembly);
    }

    private void loadEnvironment (Simulation sim, Scanner input) {
        while (input.hasNext()) {
            Scanner line = new Scanner(input.nextLine());
            if (line.hasNext()) {
                String type = line.next();
                sim.getEnvironment().add(line, type);
                // if (type.equals("gravity")) {
                // sim.getEnvironment().add(line, type);
                // }
                // else if (type.equals("viscosity")) {
                // sim.add(line, type);
                // }
                // else if (type.equals("centermass")) {
                // sim.add(line, type);
                // }
                // else if (type.equals("wall")) {
                // sim.add(line, type);
                // }
            }
        }
    }

    private Mass massCommand (Scanner line) {
        int id = line.nextInt();
        double x = line.nextDouble();
        double y = line.nextDouble();
        double mass = line.nextDouble();
        return new Mass(id, x, y, mass);
    }

    private Spring springCommand (Scanner line, Assembly assembly) {
        int m1 = line.nextInt();
        int m2 = line.nextInt();
        double restLength = line.nextDouble();
        double ks = line.nextDouble();
        if (ks >= 0)
            return new Spring((Mass) assembly.getDrawable(m1),
                    (Mass) assembly.getDrawable(m2), restLength, ks);
        else return new Bar((Mass) assembly.getDrawable(m1),
                (Mass) assembly.getDrawable(m2), restLength, ks);

    }

    private Spring muscleCommand (Scanner line, Assembly assembly) {
        int m1 = line.nextInt();
        int m2 = line.nextInt();
        double restLength = line.nextDouble();
        double ks = line.nextDouble();
        double amplitude = line.nextDouble();
        return new Muscle((Mass) assembly.getDrawable(m1),
                (Mass) assembly.getDrawable(m2), restLength, ks, amplitude);
    }
}
