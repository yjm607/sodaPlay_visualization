package mechanics;

import drawings.Mass;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * Handles environmental forces.
 * 
 * @author Jei Min Yoo, Volodymyr Zavidovych
 *
 */
public class Environment {

    private Canvas myContainer;
    private HashMap<Integer, Force> myForces = new HashMap<Integer, Force>();
    private int[] myKeyCodes =
            new int[] {KeyEvent.VK_V, KeyEvent.VK_G, KeyEvent.VK_M, KeyEvent.VK_1, KeyEvent.VK_2,
                       KeyEvent.VK_3, KeyEvent.VK_4};
    /**
     * Constructs environment
     * 
     * @param container used to communicate with Canvas instance
     */
    public Environment (Canvas container) {
        myContainer = container;
        Force blankForce = new Force();
        for (int keyCode : myKeyCodes) {
            myForces.put(keyCode, blankForce);
        }
    }

    /**
     * Receives input from the factory and processes it into information it need to create forces
     * 
     * @param line line read by the factory from the input file
     * @param type type of the input detected by the factory
     */
    public void add (Scanner line, String type) {
        if ("gravity".equals(type)) {
            GravityForce.readInputLine(line);
        }
        else if ("viscosity".equals(type)) {
            ViscosityForce.readInputLine(line);
        }
        else if ("centermass".equals(type)) {
            CenterMassForce.readInputLine(line);
        }
        else if ("wall".equals(type)) {
            WallForce.readInputLine(line);
        }
    }

    /**
     * Returns total force exerted by environment on a given mass
     * 
     * @param mass mass to apply force on
     * @param assembly assembly of the mass
     */
    public Force getAllForces (Mass mass, Assembly assembly) {
        resetAllForces(mass, assembly);
        Force totalEnvironmentForce = new Force();
        for (Map.Entry<Integer, Force> entry : myForces.entrySet()) {
            Force thisEnvironmentalForce = entry.getValue();
            Boolean isToggledOn = thisEnvironmentalForce.getToggle();
            if (isToggledOn) {
                totalEnvironmentForce.sum(thisEnvironmentalForce);
            }
        }
        return totalEnvironmentForce;
    }

    private void resetAllForces (Mass mass, Assembly assembly) {
        clearForces();
        // set forces
        myForces.put(KeyEvent.VK_G, new GravityForce(mass, myForces));
        myForces.put(KeyEvent.VK_V, new ViscosityForce(mass, myForces));
        myForces.put(KeyEvent.VK_M, new CenterMassForce(mass, assembly, myForces));
        myForces.put(KeyEvent.VK_1, new WallForce(mass, myForces, KeyEvent.VK_1, myContainer));
        myForces.put(KeyEvent.VK_2, new WallForce(mass, myForces, KeyEvent.VK_2, myContainer));
        myForces.put(KeyEvent.VK_3, new WallForce(mass, myForces, KeyEvent.VK_3, myContainer));
        myForces.put(KeyEvent.VK_4, new WallForce(mass, myForces, KeyEvent.VK_4, myContainer));
    }

    private void clearForces () {
        for (int keyCode : myKeyCodes) {
            Boolean currentForceToggle = myForces.get(keyCode).getToggle();
            Force blankForce = new Force();
            blankForce.setToggle(currentForceToggle);
            myForces.put(keyCode, blankForce);
        }
    }

    /**
     * Toggles forces on and off
     * 
     * @param keyCode toggles force by pressed key
     */
    public void toggleForce (int keyCode) {
        Force currentForce = myForces.get(keyCode);
        currentForce.setToggle(!currentForce.getToggle());
        System.out.println(KeyEvent.getKeyText(keyCode) + " is now " +
                           myForces.get(keyCode).getToggle());
    }

}
