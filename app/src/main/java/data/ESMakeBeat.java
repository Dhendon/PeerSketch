package data;

import java.io.Serializable;

/**
 * Created by davidhendon on 11/19/14.
 */
public class ESMakeBeat implements Serializable, ContainerObject {
    private static final long serialVersionUID = 1L;
    private String sampleName;
    private int sectionNumber;
    private double startLocation;
    private String beatPattern;
    private String startVariable;
    private char startOperand;
    private double startAmount;
    private boolean hasVariable;

    public ESMakeBeat(String sampleName, String beatPattern, String startVariable, char startOperand, double startAmount, int sectionNumber) {
        this.sampleName = sampleName;
        this.beatPattern = beatPattern;
        this.startVariable = startVariable;
        this.startOperand = startOperand;
        this.startAmount = startAmount;
        this.sectionNumber = sectionNumber;
        hasVariable = true;
    }

    public ESMakeBeat(String sampleName, int sectionNumber, double startLocation, String beatPattern) {
        this.sampleName = sampleName;
        this.sectionNumber = sectionNumber;
        this.startLocation = startLocation;
        this.beatPattern = beatPattern;
        hasVariable = false;
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

    public String getBeatPattern() {
        return beatPattern;
    }

    public void setBeatPattern(String beatPattern) {
        this.beatPattern = beatPattern;
    }

    public boolean isValid() {
        return true;
        //return startLocation > 0 && beatpatternIsValid(beatPattern);
    }

    public boolean beatpatternIsValid(String beatPattern) {
        String validRegexp = "((0+(\\+)*)*-)*"; //TODO: Add in the plus sign
        if (beatPattern.length() <= 1) {
            return beatPattern.equals("0") || beatPattern.equals("-");
        }
        return beatPattern.length() == Util.BEATS_IN_MEASURE && beatPattern.matches(validRegexp);

    }

    public String toString() {
        String indent = hasVariable ? "-->" : "";
        String inside = hasVariable ? sampleName + " ... " : sampleName + ", " + startLocation +
                " , \"" + beatPattern;
        return indent + "makeBeat(" + inside + "\")";
    }
}
