package data;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by davidhendon on 11/19/14.
 */
public class IfStatement extends Container implements Serializable {
    private static final long serialVersionUID = 5L;
    String firstItem;
    String secondItem;
    String conditional;
    private String TAG = "ifstatement";
    private Song parentSong;

    public IfStatement(String firstItem, String secondItem, String comparison, Song parentSong) {
        this.firstItem = firstItem;
        this.secondItem = secondItem;
        this.conditional = comparison;
        this.parentSong = parentSong;
    }

    /**
     * Evaluates this if statement
     *
     * @return true if this if statement evaluates to "true"
     * by parsing 'firstItem conditional secondItem', and this returns false when this would
     * evaluate to "false" if parsed as 'firstItem conditional secondItem'
     */
    public boolean evaluate() {
        // firstItem COMPARISON secondItem
        // TODO implement a "Statement" object that can be AND-ed or OR-ed
        // If either item is a variable, then replace it's value with the numeric value.
        double firstValue = Double.NEGATIVE_INFINITY;
        double secondValue = Double.NEGATIVE_INFINITY;
        try {
            firstValue = Double.parseDouble(firstItem);
        } catch (NumberFormatException e) {
        }
        try {
            secondValue = Double.parseDouble(firstItem);
        } catch (NumberFormatException e) {
        }

        // By now, values are assigned
        if (conditional.equals(Util.CONDITIONALS[Util.Conditionals.EQUAL])) {
            return firstValue == secondValue;
        } else if (conditional.equals((Util.CONDITIONALS[Util.Conditionals.NOTEQUAL]))) {
            return firstValue != secondValue;
        } else if (conditional.equals((Util.CONDITIONALS[Util.Conditionals.GREATERTHAN]))) {
            return firstValue > secondValue;
        } else if (conditional.equals((Util.CONDITIONALS[Util.Conditionals.GREATERTHANOREQUAL]))) {
            return firstValue >= secondValue;
        } else if (conditional.equals((Util.CONDITIONALS[Util.Conditionals.LESSTHAN]))) {
            return firstValue < secondValue;
        } else if (conditional.equals((Util.CONDITIONALS[Util.Conditionals.LESSTHANOREQUAL]))) {
            return firstValue <= secondValue;
        } else {
            Log.e(TAG, "Unrecognized conditional: " + conditional);
            return false;
        }
    }

    public String getFirstItem() {
        return firstItem;
    }

    public String getSecondItem() {
        return secondItem;
    }

    public void setSecondItem(String secondItem) {
        this.secondItem = secondItem;
    }

    public String getConditional() {
        return conditional;
    }

    public void setConditional(String comparison) {
        this.conditional = comparison;
    }

}
