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
    private String startVariable;
    private char startOperand;
    private double startAmount;
    private String endVariable;
    private char endOperand;
    private double endAmount;
    private boolean hasVariable;

    public ESFitMedia(String sampleName, int sectionNumber, double startLocation, double endLocation) {
        this.sampleName = sampleName;
        this.sectionNumber = sectionNumber;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        hasVariable = false;

    }

    public ESFitMedia(String sampleName, int sectionNumber, String startVariable, char startOperand,
                      double startAmount, String endVariable, char endOperand, double endAmount) {
        this.sampleName = sampleName;
        this.sectionNumber = sectionNumber;
        this.startVariable = startVariable;
        this.startOperand = startOperand;
        this.startAmount = startAmount;
        this.endVariable = endVariable;
        this.endOperand = endOperand;
        this.endAmount = endAmount;
        hasVariable = true;
    }

    public boolean hasVariable() {
        return hasVariable;
    }

    public String getStartVariable() {
        return startVariable;
    }

    public void setStartVariable(String startVariable) {
        this.startVariable = startVariable;
    }

    public char getStartOperand() {
        return startOperand;
    }

    public void setStartOperand(char startOperand) {
        this.startOperand = startOperand;
    }

    public double getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(double startAmount) {
        this.startAmount = startAmount;
    }

    public String getEndVariable() {
        return endVariable;
    }

    public void setEndVariable(String endVariable) {
        this.endVariable = endVariable;
    }

    public char getEndOperand() {
        return endOperand;
    }

    public void setEndOperand(char endOperand) {
        this.endOperand = endOperand;
    }

    public double getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(double endAmount) {
        this.endAmount = endAmount;
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
        String indent = hasVariable ? "-->" : "";
        String startExpression = startOperand == 'n' || startAmount == 0 ? startVariable :
                startVariable + " " + startOperand + " " + startAmount;
        String endExpression = endOperand == 'n' || endAmount == 0 ? endVariable :
                endVariable + " " + endOperand + " " + endAmount;
        String inside = hasVariable ? startExpression + " ... " + endExpression :
                startLocation + "," + endLocation;
        return indent + sampleName + " (" + inside + ")";
    }
}
