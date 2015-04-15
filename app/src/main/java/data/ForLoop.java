package data;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidhendon on 10/28/14.
 */
public class ForLoop extends Group implements Serializable {
    private static final long serialVersionUID = 3L;
    private double start;
    private double stepSize; //TODO: check stepSize to make sure it's valid
    private double end;
    private String variable;
    private int sectionNumber;
    private char operand;
    private List<Double> iterValues;

    public ForLoop(double start, double stepSize, double end, String variable, char operand) {
        this.start = start;
        this.stepSize = stepSize;
        this.end = end;
        this.variable = variable;
        sectionNumber = -1;
        this.operand = operand;
        iterValues = new ArrayList<Double>();
    }

    public List<Double> getIterValues() {
        // TODO: IsValid checks that don't cause this to return nothing.
        iterValues = new ArrayList<Double>();
        if (operand == '+') {
            for (double i = start; i < end; i += stepSize) {
                iterValues.add(i);
                Log.i("for-loop", "+ i=" + i);
            }
        } else if (operand == '-') {
            for (double i = start; i < end; i -= stepSize) {
                iterValues.add(i);
                Log.i("for-loop", "- i=" + i);
            }
        } else {
            Log.i("for-loop", "Unknown operand");
        }

        return iterValues;
    }

    public char getOperand() {
        return operand;
    }

    public void setOperand(char operand) {
        this.operand = operand;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public double getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public double getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    // TODO: check for conflicts with other variable names?
    public boolean isValid() {
        return stepSizeIsValid() && startIsValid() && endIsValid() && !variable.equals("");
    }

    private boolean stepSizeIsValid() {
        if (stepSize == 0) { // Will loop infinitely
            return false;
        } else if (Math.abs(stepSize) > Math.abs(end - start)) { // too big
            return false;
        } else if (stepSize < 0 && end > start) { // Step decrements when it should increment
            return false;
        } else if (stepSize > 0 && end < start) { // Step increments when it should decrement
            return false;
        } else {
            return true;
        }
    }

    private boolean startIsValid() {
        if (start < 0) { // Negative start
            return false;
        } else if (start == end) { // This case is actually handled by stepSizeIsValid.
            return false;
        } else { // TODO: Check start versus max song length.
            return true;
        }
    }

    private boolean endIsValid() {
        if (end < 0) { // Negative end
            return false;
        } else if (end == start) { // This case is actually handled by stepSizeIsValid.
            return false;
        } else { // TODO: Ensure max is less than max song length.
            return true;
        }
    }

    @Override
    public String toString() {
        String base = "for " + variable + " in range(" + start + ", " + end + ", " + stepSize + ")";
        String contained = "";
        for (GroupObject object : orderedObjects) {
            contained += "\n-->" + object.toString();
        }
        return base + contained;
    }

    // Python does not have conditionals within their for loops..
    /*private boolean conditionalIsValid() {
        boolean increasing = stepSize > 0;
        boolean largerEnd = start < end;
        if (conditional == Util.Conditionals.EQUAL) { // Nope.
            return false;
        } else if (conditional == Util.Conditionals.GREATERTHAN
                || conditional == Util.Conditionals.GREATERTHANOREQUAL
                && !increasing && !largerEnd) {
            return true;
        } else if (conditional == Util.Conditionals.LESSTHAN
                || conditional == Util.Conditionals.LESSTHANOREQUAL && increasing && largerEnd) {
            return true;
        } else {
            return false;
        }
    }*/
}
