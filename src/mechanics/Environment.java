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
    private ArrayList<Force> myForceList = new ArrayList<Force>();
    private Force myGravityForce;
    private Force myViscosityForce;
    private Force myCenterMassForce;
    private Force myWallForce1;
    private Force myWallForce2;
    private Force myWallForce3;
    private Force myWallForce4;

    private double myGravityAngle;
    private double myGravityMagnitude;
    private double myViscosity;
    private double myCenterMassForceMagnitude;
    private double myCenterMassExponent;
    private HashMap<Force, Double> myRawWallForces;
    private HashMap<Integer, Boolean> forceToggles;

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
        myWallForce1 = new Force();
        myWallForce2 = new Force();
        myWallForce3 = new Force();
        myWallForce4 = new Force();
        forceToggles = new HashMap<Integer, Boolean>();
        forceToggles.put(KeyEvent.VK_V, true);
        forceToggles.put(KeyEvent.VK_G, true);
        forceToggles.put(KeyEvent.VK_M, true);
        forceToggles.put(KeyEvent.VK_1, true);
        forceToggles.put(KeyEvent.VK_2, true);
        forceToggles.put(KeyEvent.VK_3, true);
        forceToggles.put(KeyEvent.VK_4, true);
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
            HashMap<Force, Double> result = new HashMap<Force, Double>();
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
        toggleForces();
        Force totalEnvironmentForce = new Force();
        for (Force f : myForceList) {
            totalEnvironmentForce.sum(f);
        }
        return totalEnvironmentForce;
    }

    private void toggleForces() {
        if (!forceToggles.get(KeyEvent.VK_V)) {
            myForceList.remove(myViscosityForce);
        }
        if (!forceToggles.get(KeyEvent.VK_G)) {
            myForceList.remove(myGravityForce);
        }
        if (!forceToggles.get(KeyEvent.VK_M)) {
            myForceList.remove(myCenterMassForce);
        }
        if (!forceToggles.get(KeyEvent.VK_1)) {
            myForceList.remove(myWallForce1);
        }
        if (!forceToggles.get(KeyEvent.VK_2)) {
            myForceList.remove(myWallForce2);
        }
        if (!forceToggles.get(KeyEvent.VK_3)) {
            myForceList.remove(myWallForce3);
        }
        if (!forceToggles.get(KeyEvent.VK_4)) {
            myForceList.remove(myWallForce4);
        }
    }

    private void resetAllForces (Mass mass, Assembly assembly) {
        myForceList.clear();
        setGravity(mass, myGravityAngle, myGravityMagnitude);
        setViscosity(myViscosity, mass);
        setCenterMassForce(mass, assembly);
        setWallForce(mass);
    }

    private void setGravity (Mass mass, double angle, double magnitude) {
        myGravityForce = new Force(angle, magnitude);
        myGravityForce.scale(mass.getMass());
        myForceList.add(myGravityForce);
    }

    private void setViscosity (double viscosity, Mass mass) {
        Force velocity = new Force(mass.getVelocity());
        velocity.negate();
        myViscosityForce = new Force(velocity.getDirection(),
                velocity.getMagnitude() * viscosity);
        myForceList.add(myViscosityForce);
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
        myCenterMassForce = new Force(angle, magnitude);
        myForceList.add(myCenterMassForce);
    }

    private void setWallForce (Mass mass) {
        for (Map.Entry<Force, Double> entry : myRawWallForces.entrySet()) {
            Force oneWallForce = new Force(entry.getKey());
            double exponent = entry.getValue();
            double distance = 0;
            switch ((int) oneWallForce.getDirection()) {
                case 90:
                    distance = mass.getCenter().getY()
                            / Canvas.CENTER_MASS_FORCE_DISTANCE_DIVIDER;
                    oneWallForce.scale(1 / Math.pow(distance, exponent));
                    myWallForce1 = oneWallForce;
                    break;
                case 180:
                    distance = (myContainer.getSize().width - mass.getCenter()
                            .getX())
                            / Canvas.CENTER_MASS_FORCE_DISTANCE_DIVIDER;
                    oneWallForce.scale(1 / Math.pow(distance, exponent));
                    myWallForce2 = oneWallForce;
                    break;
                case 270:
                    distance = (myContainer.getSize().height - mass.getCenter()
                            .getY())
                            / Canvas.CENTER_MASS_FORCE_DISTANCE_DIVIDER;
                    oneWallForce.scale(1 / Math.pow(distance, exponent));
                    myWallForce3 = oneWallForce;
                    break;

                case 0:
                    distance = mass.getCenter().getX()
                            / Canvas.CENTER_MASS_FORCE_DISTANCE_DIVIDER;
                    oneWallForce.scale(1 / Math.pow(distance, exponent));
                    myWallForce4 = oneWallForce;
                    break;
            }
        }
        myForceList.add(myWallForce1);
        myForceList.add(myWallForce2);
        myForceList.add(myWallForce3);
        myForceList.add(myWallForce4);
    }

    public void toggleForce (int keyCode) {
        forceToggles.put(keyCode, !forceToggles.get(keyCode));
    }

}
