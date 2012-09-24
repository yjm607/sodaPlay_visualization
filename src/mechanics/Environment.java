package mechanics;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import drawings.Drawable;
import drawings.Mass;


public class Environment {

    private Canvas myContainer;
    private HashMap<Integer, Object[]> myForces = new HashMap<Integer, Object[]>();
    private int[] keyCodes = new int[] { KeyEvent.VK_V, KeyEvent.VK_G,
            KeyEvent.VK_M, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3,
            KeyEvent.VK_4 };

    private double myGravityAngle;
    private double myGravityMagnitude;
    private double myViscosity;
    private double myCenterMassForceMagnitude;
    private double myCenterMassExponent;
    private HashMap<Force, Double> myRawWallForces;

    public Environment (Canvas container) {
        myContainer = container;
        initializeDefaults();
    }

    private void initializeDefaults () {
        myGravityAngle = 0;
        myGravityMagnitude = 0;
        myViscosity = 0;
        myCenterMassForceMagnitude = 0;
        myCenterMassExponent = 0;
        myRawWallForces = new HashMap<Force, Double>();
        Object[] blankForce = new Object[] { new Force(), true };
        for (int i = 0; i < keyCodes.length; i++) {
            myForces.put(keyCodes[i], blankForce);
        }
    }

    public void add (Scanner line, String type) {
        if (type.equals("gravity")) {
            myGravityAngle = line.nextDouble();
            myGravityMagnitude = line.nextDouble();
        }
        else if (type.equals("viscosity")) {
            myViscosity = line.nextDouble();
        }
        else if (type.equals("centermass")) {
            myCenterMassForceMagnitude = line.nextDouble();
            myCenterMassExponent = line.nextDouble();
        }
        else if (type.equals("wall")) {
            int id = line.nextInt();
            double magnitude = line.nextDouble();
            double exponent = line.nextDouble();
            Force force = new Force();
            switch (id) {
                case 1:
                    force = new Force(90, magnitude);
                    break;
                case 2:
                    force = new Force(180, magnitude);
                    break;
                case 3:
                    force = new Force(270, magnitude);
                    break;
                case 4:
                    force = new Force(0, magnitude);
                    break;
            }
            myRawWallForces.put(force, exponent);
        }

    }

    public Force getAllForces (Mass mass, Assembly assembly) {
        resetAllForces(mass, assembly);
        Force totalEnvironmentForce = new Force();
        for (Map.Entry<Integer, Object[]> entry : myForces.entrySet()) {
            Force thisEnvironmentalForce = (Force) entry.getValue()[0];
            Boolean isToggledOn = (Boolean) entry.getValue()[1];
            if (isToggledOn) totalEnvironmentForce.sum(thisEnvironmentalForce);
        }
        for (int i = 0; i < keyCodes.length; i++) {
            // System.out.println(keyCodes[i] + " " +
            // myForces.get(keyCodes[i])[0]
            // + " is " + myForces.get(keyCodes[i])[1] + " for mass "
            // + mass.getMass());
        }

        return totalEnvironmentForce;
    }

    private void resetAllForces (Mass mass, Assembly assembly) {
        clearForces();
        setGravity(mass, myGravityAngle, myGravityMagnitude);
        setViscosity(myViscosity, mass);
        setCenterMassForce(mass, assembly);
        setWallForce(mass);
    }

    private void clearForces () {
        for (int i = 0; i < keyCodes.length; i++) {
            Boolean currentForceToggle = (Boolean) myForces.get(keyCodes[i])[1];
            Object[] blankForce = new Object[] { new Force(),
                    currentForceToggle };
            myForces.put(keyCodes[i], blankForce);
        }
    }

    private void setGravity (Mass mass, double angle, double magnitude) {
        Force gravityForce = new Force(angle, magnitude);
        gravityForce.scale(mass.getMass());
        Boolean currentToggle = (Boolean) myForces.get(KeyEvent.VK_G)[1];
        myForces.put(KeyEvent.VK_G,
                new Object[] { gravityForce, currentToggle });
    }

    private void setViscosity (double viscosity, Mass mass) {
        Force velocity = new Force(mass.getVelocity());
        velocity.negate();
        Force viscosityForce = new Force(velocity.getDirection(),
                velocity.getMagnitude() * viscosity);
        Boolean currentToggle = (Boolean) myForces.get(KeyEvent.VK_V)[1];
        myForces.put(KeyEvent.VK_V, new Object[] { viscosityForce,
                currentToggle });
    }

    private void setCenterMassForce (Mass mass, Assembly assembly) {
        double xCenter = 0;
        double yCenter = 0;
        double totalMass = 0;
        for (Drawable d : assembly.getMyDrawings()) {
            if (d.getClassName().equals("mass")) {
                xCenter += ((Mass) d).getMass() * ((Mass) d).getCenter().getX();
                yCenter += ((Mass) d).getMass() * ((Mass) d).getCenter().getY();
                totalMass += ((Mass) d).getMass();
            }
        }
        double dx = xCenter / totalMass - mass.getCenter().getX();
        double dy = yCenter / totalMass - mass.getCenter().getY();
        double angle = Force.angleBetween(dx, dy);
        double distance = Force.distanceBetween(dx, dy)
                / Canvas.CENTER_MASS_FORCE_DISTANCE_DIVIDER;
        double magnitude = myCenterMassForceMagnitude
                / Math.pow(distance, myCenterMassExponent);
        Force centerMassForce = new Force(angle, magnitude);
        Boolean currentToggle = (Boolean) myForces.get(KeyEvent.VK_M)[1];
        myForces.put(KeyEvent.VK_M, new Object[] { centerMassForce,
                currentToggle });
    }

    private void setWallForce (Mass mass) {
        for (Map.Entry<Force, Double> entry : myRawWallForces.entrySet()) {
            Force oneWallForce = new Force(entry.getKey());
            double exponent = entry.getValue();
            double distance = 0;
            Boolean currentForceToggle;
            switch ((int) oneWallForce.getDirection()) {
                case 90:
                    distance = mass.getCenter().getY()
                            / Canvas.CENTER_MASS_FORCE_DISTANCE_DIVIDER;
                    oneWallForce.scale(1 / Math.pow(distance, exponent));
                    currentForceToggle = (Boolean) myForces.get(KeyEvent.VK_1)[1];
                    myForces.put(KeyEvent.VK_1, new Object[] { oneWallForce,
                            currentForceToggle });
                    break;
                case 180:
                    distance = (myContainer.getSize().width - mass.getCenter()
                            .getX())
                            / Canvas.CENTER_MASS_FORCE_DISTANCE_DIVIDER;
                    oneWallForce.scale(1 / Math.pow(distance, exponent));
                    currentForceToggle = (Boolean) myForces.get(KeyEvent.VK_2)[1];
                    myForces.put(KeyEvent.VK_2, new Object[] { oneWallForce,
                            currentForceToggle });
                    break;
                case 270:
                    distance = (myContainer.getSize().height - mass.getCenter()
                            .getY())
                            / Canvas.CENTER_MASS_FORCE_DISTANCE_DIVIDER;
                    oneWallForce.scale(1 / Math.pow(distance, exponent));
                    currentForceToggle = (Boolean) myForces.get(KeyEvent.VK_3)[1];
                    myForces.put(KeyEvent.VK_3, new Object[] { oneWallForce,
                            currentForceToggle });
                    break;

                case 0:
                    distance = mass.getCenter().getX()
                            / Canvas.CENTER_MASS_FORCE_DISTANCE_DIVIDER;
                    oneWallForce.scale(1 / Math.pow(distance, exponent));
                    currentForceToggle = (Boolean) myForces.get(KeyEvent.VK_4)[1];
                    myForces.put(KeyEvent.VK_4, new Object[] { oneWallForce,
                            currentForceToggle });
                    break;
            }
        }
    }

    public void toggleForce (int keyCode) {
        Boolean currentForceToggle = (Boolean) myForces.get(keyCode)[1];
        Force currentForce = (Force) myForces.get(keyCode)[0];
        myForces.put(keyCode,
                new Object[] { currentForce, !currentForceToggle });
        System.out.println(KeyEvent.getKeyText(keyCode) + " is now " + myForces.get(keyCode)[1]
                + ", was " + currentForceToggle.toString());
    }

}
