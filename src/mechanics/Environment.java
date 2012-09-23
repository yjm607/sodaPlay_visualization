package mechanics;

import java.util.ArrayList;
import java.util.Scanner;
import drawings.Drawable;
import drawings.Mass;


public class Environment {

    private ArrayList<Force> myForceList = new ArrayList<Force>();
    private ArrayList<Drawable> myDrawings = new ArrayList<Drawable>();
    private Force myGravityForce;
    private Force myViscosityForce;
    private Force myCenterMassForce;
    private Force myWallForce;

    private double myGravityAngle;
    private double myGravityMagnitude;
    private double myViscosity;
    private double myCenterMassForceMagnitude;
    private double myCenterMassExponent;

    public Environment () {
        myGravityAngle = 0;
        myGravityMagnitude = 0;
        myViscosity = 0;
        myCenterMassForceMagnitude = 0;
        myCenterMassExponent = 0;
    }

    public void addDrawing (Drawable d) {
        myDrawings.add(d);
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
            setWallForce(line);
        }

    }

    public Force getAllForces (Mass mass) {
        resetAllForces(mass);
        Force totalEnvironmentForce = new Force();
        for (Force f : myForceList) {
            totalEnvironmentForce.sum(f);
        }
        return totalEnvironmentForce;
    }

    private void resetAllForces (Mass mass) {
        myForceList.clear();
        setGravity(mass, myGravityAngle, myGravityMagnitude);
        setViscosity(myViscosity, mass);
        setCenterMassForce(mass);
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

    private void setCenterMassForce (Mass mass) {
        double xCenter = 0;
        double yCenter = 0;
        double totalMass = 0;
        for (Drawable d : myDrawings) {
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

    private void setWallForce (Scanner line) {
        myForceList.add(myWallForce);
    }

}
