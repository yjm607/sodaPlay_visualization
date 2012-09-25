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

    private double myGravityAngle;
    private double myGravityMagnitude;
    private double myViscosity;
    private double myCenterMassMagnitude;
    private double myCenterMassExponent;
    private HashMap<Force, Double> myRawWallForces;

    /**
     * Constructs environment
     * 
     * @param container used to communicate with Canvas instance
     */
    public Environment (Canvas container) {
        myContainer = container;
        initializeDefaults();
    }

    private void initializeDefaults () {
        myGravityAngle = 0;
        myGravityMagnitude = 0;
        myViscosity = 0;
        myCenterMassMagnitude = 0;
        myCenterMassExponent = 0;
        myRawWallForces = new HashMap<Force, Double>();
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
            myGravityAngle = line.nextDouble();
            myGravityMagnitude = line.nextDouble();
        }
        else if ("viscosity".equals(type)) {
            myViscosity = line.nextDouble();
        }
        else if ("centermass".equals(type)) {
            myCenterMassMagnitude = line.nextDouble();
            myCenterMassExponent = line.nextDouble();
        }
        else if ("wall".equals(type)) {
            int id = line.nextInt();
            double magnitude = line.nextDouble();
            double exponent = line.nextDouble();
            Force force = new Force();
            switch (id) {
                case 1:
                    force = new Force(Canvas.DOWN_ANGLE, magnitude);
                    break;
                case 2:
                    force = new Force(Canvas.LEFT_ANGLE, magnitude);
                    break;
                case 3:
                    force = new Force(Canvas.UP_ANGLE, magnitude);
                    break;
                case 4:
                    force = new Force(Canvas.RIGHT_ANGLE, magnitude);
                    break;
                default:
                    break;
            }
            myRawWallForces.put(force, exponent);
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
        setGravity(mass);
        setViscosity(mass);
        setCenterMassForce(mass, assembly);
        setWallForce(mass);
    }

    private void clearForces () {
        for (int keyCode : myKeyCodes) {
            Boolean currentForceToggle = myForces.get(keyCode).getToggle();
            Force blankForce = new Force();
            blankForce.setToggle(currentForceToggle);
            myForces.put(keyCode, blankForce);
        }
    }

    private void setGravity (Mass mass) {
        boolean currentToggle = myForces.get(KeyEvent.VK_G).getToggle();
        Force gravityForce =
                new GravityForce(mass, myGravityAngle, myGravityMagnitude, currentToggle);
        myForces.put(KeyEvent.VK_G, gravityForce);
    }

    private void setViscosity (Mass mass) {
        boolean currentToggle = myForces.get(KeyEvent.VK_V).getToggle();
        Force viscosityForce = new ViscosityForce(mass, myViscosity, currentToggle);
        myForces.put(KeyEvent.VK_V, viscosityForce);
    }

    private void setCenterMassForce (Mass mass, Assembly assembly) {
        boolean currentToggle = myForces.get(KeyEvent.VK_M).getToggle();
        Force centerMassForce =
                new CenterMassForce(mass, assembly, myCenterMassMagnitude, myCenterMassExponent,
                                    currentToggle);
        myForces.put(KeyEvent.VK_M, centerMassForce);
    }

    private void setWallForce (Mass mass) {
        for (Map.Entry<Force, Double> entry : myRawWallForces.entrySet()) {
            WallForce wallForce =
                    new WallForce(mass, entry.getKey(), entry.getValue(), myForces, myContainer);
            myForces.put(wallForce.getKeyCode(), wallForce);
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
