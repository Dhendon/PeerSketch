package data;

import java.io.Serializable;

/**
 * Created by hendon on 11/19/14.
 */
public class ESFitMedia implements Serializable, GroupObject {
    private static final long serialVersionUID = 0L;
    private String sampleName;
    private int sectionNumber;
    private double startLocation;
    private double endLocation;

    public ESFitMedia(String sampleName, int sectionNumber, double startLocation, double endLocation) {
        this.sampleName = sampleName;
        this.sectionNumber = sectionNumber;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    // TODO: Implement this FitMedia isValid method
    public boolean isValid() {
        // Check sample name is correct / exists
        // make sure track exists
        if (startLocation > endLocation) {
            return false;
        } else if (startLocation < 0 || endLocation < 0) {
            return false;
        } else { // TODO: Add max song length.
            return true;
        }
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public double getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(double startLocation) {
        this.startLocation = startLocation;
    }

    public double getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(double endLocation) {
        this.endLocation = endLocation;
    }

    @Override
    public String toString() {
        return sampleName + " (" + startLocation + "," + endLocation + ")";
    }
}
