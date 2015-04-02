package data;

import java.io.Serializable;

/**
 * Created by davidhendon on 10/28/14.
 */
public class ESSetEffect implements Serializable, GroupObject {
    private static final long serialVersionUID = 2L;
    private int effectIndex;
    // TODO: replace effectIndex with effectName
    private int sectionNumber;
    private double startMeasure;
    private double endMeasure;
    private double amount;

    /**
     * @param effectIndex
     * @param startMeasure
     * @param endMeasure
     * @param amount       the scale on the effect, from (0.0, 1.0]
     */
    public ESSetEffect(int effectIndex, double startMeasure, double endMeasure, double amount) {
        this.effectIndex = effectIndex;
        this.startMeasure = startMeasure;
        this.endMeasure = endMeasure;
        this.amount = amount;
    }

    public double getStartMeasure() {
        return startMeasure;
    }

    public void setStartMeasure(double startMeasure) {
        this.startMeasure = startMeasure;
    }

    public double getEndMeasure() {
        return endMeasure;
    }

    public void setEndMeasure(double endMeasure) {
        this.endMeasure = endMeasure;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getEffectIndex() {
        return effectIndex;
    }

    public void setEffectIndex(int effectIndex) {
        this.effectIndex = effectIndex;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    // TODO Implement this SetEffect isValid method.
    public boolean isValid() {
        boolean validMeasures = startMeasure < endMeasure;
        boolean validEffectIndex = effectIndex >= 0 && effectIndex < Util.EFFECT_NAMES.length;
        return validMeasures && validEffectIndex;
    }

    @Override
    public String toString() {
        return "setEffect(" + Util.EFFECT_NAMES[effectIndex] +
                " " + sectionNumber +
                " (" + startMeasure +
                ", " + endMeasure +
                ") amount=" + amount;
    }
}
