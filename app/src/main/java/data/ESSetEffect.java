package data;

import java.io.Serializable;

/**
 * Created by davidhendon on 10/28/14.
 */
public class ESSetEffect implements Serializable, GroupObject {
    private static final long serialVersionUID = 2L;
    private int effectIndex;
    private int trackNumber;
    private double measure;
    private double effectValue;

    public ESSetEffect(int effectIndex, int trackNumber, double measure) {
        this.effectIndex = effectIndex;
        this.trackNumber = trackNumber;
        this.measure = measure;
    }

    public double getEffectValue() {
        return effectValue;
    }

    public void setEffectValue(double effectValue) {
        this.effectValue = effectValue;
    }

    public int getEffectIndex() {
        return effectIndex;
    }

    public void setEffectIndex(int effectIndex) {
        this.effectIndex = effectIndex;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public double getMeasure() {
        return measure;
    }

    public void setMeasure(double measure) {
        this.measure = measure;
    }

    // TODO Implement this SetEffect isValid method.
    public boolean isValid() {
        return true;
    }
}
