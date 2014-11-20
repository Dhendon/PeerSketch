package data;

/**
 * Created by davidhendon on 11/19/14.
 */
public class IfStatement {
    static final String[] COMPARISONS = {"=", "!=", ">", "<", "<=", ">="};
    String firstItem;
    String secondItem;
    String comparison;

    public IfStatement(String firstItem, String secondItem, String comparison) {
        this.firstItem = firstItem;
        this.secondItem = secondItem;
        this.comparison = comparison;
    }

    /**
     * Evaluates this if statement
     *
     * @return true if this if statement evaluates to "true"
     * by parsing 'firstItem comparison secondItem', and this returns false when this would
     * evaluate to "false" if parsed as 'firstItem comparison secondItem'
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

    public String getComparison() {
        return comparison;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
    }

}
