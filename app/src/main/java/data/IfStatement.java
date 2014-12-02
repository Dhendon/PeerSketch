package data;

import java.io.Serializable;

/**
 * Created by davidhendon on 11/19/14.
 */
public class IfStatement implements Serializable {
    private static final long serialVersionUID = 5L;
    String firstItem;
    String secondItem;
    String conditional;

    public IfStatement(String firstItem, String secondItem, String comparison) {
        this.firstItem = firstItem;
        this.secondItem = secondItem;
        this.conditional = comparison;
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
        // TODO implement this, probably using case statements, or just make them objects.
        return false;
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
