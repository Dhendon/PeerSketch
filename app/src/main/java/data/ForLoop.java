package data;

/**
 * Created by davidhendon on 10/28/14.
 */
public class ForLoop {
    private int start;
    private int stepSize; //TODO: check stepSize to make sure it's valid
    private int end;
    private int conditional;

    // TODO: Determine conditionals?

    public ForLoop(int start, int stepSize, int end, int conditional) {
        this.start = start;
        this.stepSize = stepSize;
        this.end = end;
        this.conditional = conditional;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isValid() {
        return stepSizeIsValid() && startIsValid() && endIsValid() && conditionalIsValid();
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

    // TODO: Decide whether or not to turn red for 'equals.'
    private boolean conditionalIsValid() {
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
    }
}
